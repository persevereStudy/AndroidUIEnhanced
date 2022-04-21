package com.dz.dzbook.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.dz.dzbook.ui.JFAttrReader;
import com.dz.dzbook.ui.material.shadow.CutCornerTreatment;
import com.dz.dzbook.ui.material.shadow.MaterialShapeDrawable;
import com.dz.dzbook.ui.material.shadow.RoundedCornerTreatment;
import com.dz.dzbook.ui.material.shadow.ShapeAppearanceModel;
import com.dz.dzbook.ui.view.GradientOrientation;
import com.dz.dzbook.ui.view.GradientView;
import com.dz.dzbook.ui.view.ShadowView;
import com.dz.dzbook.ui.view.ShapeModelView;
import com.dz.dzbook.ui.view.StrokeView;


/**
 * @author wj
 *  * Date 2022/4/18 3:46 下午
 * View增强，暂不支持9.0以下阴影变色
 */
public class ViewEnhanceHelper implements
        ShapeModelView,
        ShadowView,
        StrokeView,
        GradientView {

    private Context context;
    private View rootView;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public ViewEnhanceHelper(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    public void readAttrs(AttributeSet attrs, int[] styleable, int defStyleAttr) {
        initBackground();
        TypedArray a = context.obtainStyledAttributes(attrs, styleable, defStyleAttr, 0);

        JFAttrReader.initElevation((ShadowView) rootView, a, ViewEnhanceIds.getElevationIds(styleable));
        JFAttrReader.initStroke((StrokeView) rootView, a, ViewEnhanceIds.getStrokeIds(styleable));
        JFAttrReader.initCornerCutRadius((ShapeModelView) rootView, a, ViewEnhanceIds.getCornerCutRadiusIds(styleable));
        JFAttrReader.initGradient((GradientView) rootView, a, ViewEnhanceIds.getGradientIds(styleable));

        a.recycle();

        if (rootView instanceof ViewGroup) {
            ((ViewGroup) rootView).setClipToPadding(false);
        }
    }

    // -------------------------------
    // Gradient
    // -------------------------------
    private int solidColor;
    private int gradientStartColor;
    private int gradientEndColor;
    private int gradientOrientation;
    private GradientDrawable gradientDrawable;

    @Override
    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
        //由于填充和渐变切换有问题，所以实现填充实现也用渐变
        gradientDrawable.setOrientation(GradientOrientation.getGradientOrientation(gradientOrientation));
        gradientDrawable.setColors(new int[]{solidColor, solidColor});
    }

    @Override
    public int getSolidColor() {
        return solidColor;
    }

    @Override
    public void setGradientColor(int startColor, int endColor) {
        gradientStartColor = startColor;
        gradientEndColor = endColor;
        if (gradientOrientation != GradientOrientation.NONE)
            gradientDrawable.setColors(new int[]{gradientStartColor, gradientEndColor});
    }

    @Override
    public int[] getGradientColor() {
        return new int[]{gradientStartColor, gradientEndColor};
    }

    @Override
    public void setGradientOrientation(int orientation) {
        gradientOrientation = orientation;
        if (gradientOrientation != GradientOrientation.NONE)
            gradientDrawable.setOrientation(GradientOrientation.getGradientOrientation(gradientOrientation));
    }

    @Override
    public int getGradientOrientation() {
        return gradientOrientation;
    }

    private void initBackground() {
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        rootView.setBackground(gradientDrawable);
    }


    public float elevation = 0;
    public float translationZ = 0;
    private ShapeAppearanceModel shapeModel = new ShapeAppearanceModel();
    private MaterialShapeDrawable shadowDrawable = new MaterialShapeDrawable(shapeModel);
    public ColorStateList ambientShadowColor, spotShadowColor;
    private boolean shadowCanvasEnable;

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public void setTranslationZ(float translationZ) {
        this.translationZ = translationZ;
    }

    @Override
    public boolean hasShadow() {
        return getElevation() + getTranslationZ() >= 0.01f && rootView.getWidth() > 0 && rootView.getHeight() > 0;
    }

    @Override
    public void drawShadow(Canvas canvas) {
        if (shadowCanvasEnable) {   //默认false，列表中慎用（耗时）
            drawShadowCanvas(canvas);
        } else {
            drawShadowBitmap(canvas);
        }
    }

    //.9图展示通用阴影
    private void drawShadowBitmap(Canvas canvas) {
        long start = System.currentTimeMillis();
        float z = getElevation() + getTranslationZ();
        Drawable shadowDrawable1 = JFAttrReader.getShadowDrawable(rootView.getContext());
        shadowDrawable1.setAlpha(0x90);
        shadowDrawable1.setBounds((int) (rootView.getLeft() - z), (int) (rootView.getTop() - z), (int) (rootView.getRight() + z), (int) (rootView.getBottom() + z));
        shadowDrawable1.draw(canvas);
        long end = System.currentTimeMillis();
        Log.d("耗时", "drawShadowBitmap " + (end - start) + "ms");
    }

    //自己绘制阴影
    private void drawShadowCanvas(Canvas canvas) {
        long start = System.currentTimeMillis();
//
        float alpha = rootView.getAlpha() * JFAttrReader.getDrawableAlpha(rootView.getBackground()) / 255.0f;
        if (alpha == 0 || !hasShadow())
            return;

        float z = getElevation() + getTranslationZ();

        int saveCount;
        boolean maskShadow = rootView.getBackground() != null && alpha != 1;

        paint.setAlpha((int) (127 * alpha));
        saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        Matrix matrix = rootView.getMatrix();

        shadowDrawable.setTintList(spotShadowColor);
        shadowDrawable.setAlpha(0x44);
        shadowDrawable.setElevation(z);
        shadowDrawable.setBounds(rootView.getLeft(), (int) (rootView.getTop() + z / 2), rootView.getRight(), (int) (rootView.getBottom() + z / 2));
        shadowDrawable.draw(canvas);

        if (saveCount != 0) {
            canvas.translate(rootView.getLeft(), rootView.getTop());
            canvas.concat(matrix);
            paint.setXfermode(JFAttrReader.CLEAR_MODE);
        }


        if (maskShadow) {
            cornersMask.setFillType(Path.FillType.WINDING);
            canvas.drawPath(cornersMask, paint);
        }

        if (saveCount != 0) {
            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
            paint.setAlpha(255);
        }

        long end = System.currentTimeMillis();
        Log.d("耗时", "drawShadowCanvas " + (end - start) + "ms");
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        ambientShadowColor = spotShadowColor = shadowColor;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        ambientShadowColor = spotShadowColor = ColorStateList.valueOf(color);
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return ambientShadowColor;
    }

    @Override
    public void setOutlineAmbientShadowColor(int color) {
        setOutlineAmbientShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineAmbientShadowColor(ColorStateList color) {
        ambientShadowColor = color;
    }

    @Override
    public int getOutlineAmbientShadowColor() {
        return ambientShadowColor.getDefaultColor();
    }

    @Override
    public void setOutlineSpotShadowColor(int color) {
        setOutlineSpotShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineSpotShadowColor(ColorStateList color) {
        spotShadowColor = color;
    }

    @Override
    public int getOutlineSpotShadowColor() {
        return spotShadowColor.getDefaultColor();
    }

    @Override
    public void setShadowCanvasEnable(boolean shadowCanvasEnable) {
        this.shadowCanvasEnable = shadowCanvasEnable;
    }

    // -------------------------------
    // shape
    // -------------------------------
    private Rect boundsRect = new Rect();
    private Path cornersMask = new Path();
    private boolean isEnableCrop = false;

    @Override
    public ShapeAppearanceModel getShapeModel() {
        return shapeModel;
    }

    @Override
    public void setCornerRadius(float cornerRadius) {
        shapeModel.setAllCorners(new RoundedCornerTreatment(cornerRadius));
        setShapeModel(shapeModel);
    }

    @Override
    public void setCornerCut(float cornerCut) {
        shapeModel.setAllCorners(new CutCornerTreatment(cornerCut));
        setShapeModel(shapeModel);
    }

    @Override
    public void setCornerRadii(float topLeftCornerRadius, float topRightCornerRadius, float bottomRightCornerRadius, float bottomLeftCornerRadius) {
        shapeModel.setCornerRadii(topLeftCornerRadius, topRightCornerRadius, bottomRightCornerRadius, bottomLeftCornerRadius);
        setShapeModel(shapeModel);
    }

    @Override
    public void setShapeModel(ShapeAppearanceModel model) {
        if (!JFAttrReader.IS_LOLLIPOP_OR_HIGHER)
            rootView.postInvalidate();
        this.shapeModel = model;
        if (rootView.getWidth() > 0 && rootView.getHeight() > 0)
            updateCorners();
        //更新bg
        updateBgCorner();
    }

    private void updateBgCorner() {
        if (shapeModel.isRoundRect()) {
//            gradientDrawable.setCornerRadius(shapeModel.getTopLeftCorner().getCornerSize());
            float[] radii = new float[8];
            radii[0] = radii[1] = shapeModel.getTopLeftCorner().getCornerSize();
            radii[2] = radii[3] = shapeModel.getTopRightCorner().getCornerSize();
            radii[4] = radii[5] = shapeModel.getBottomRightCorner().getCornerSize();
            radii[6] = radii[7] = shapeModel.getBottomLeftCorner().getCornerSize();
            gradientDrawable.setCornerRadii(radii);
        } else {
            float[] radii = new float[8];
            radii[0] = radii[1] = shapeModel.getTopLeftCorner().getCornerSize();
            radii[2] = radii[3] = shapeModel.getTopRightCorner().getCornerSize();
            radii[4] = radii[5] = shapeModel.getBottomRightCorner().getCornerSize();
            radii[6] = radii[7] = shapeModel.getBottomLeftCorner().getCornerSize();
            gradientDrawable.setCornerRadii(radii);
        }
    }

    @Override
    public void setEnableCrop(boolean isEnableCrop) {
        this.isEnableCrop = isEnableCrop;
    }

    // -------------------------------
    // stroke
    // -------------------------------

    private ColorStateList stroke;
    private float strokeWidth;

    @Override
    public void setStroke(ColorStateList colorStateList) {
        stroke = colorStateList;

        if (stroke == null)
            return;
        if (strokeWidth > 0)
            gradientDrawable.setStroke((int) strokeWidth, stroke.getDefaultColor());
    }

    @Override
    public void setStroke(int color) {
        setStroke(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getStroke() {
        return stroke;
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (strokeWidth > 0 && stroke != null)
            gradientDrawable.setStroke((int) strokeWidth, stroke.getDefaultColor());
    }

    @Override
    public float getStrokeWidth() {
        return strokeWidth;
    }

    //draw
    public void draw(Canvas canvas, DrawCallback drawCallback) {
        long start = System.currentTimeMillis();

        boolean c = !JFAttrReader.isShapeRect(shapeModel);

        if (rootView.getWidth() > 0 && rootView.getHeight() > 0 && ((c && !JFAttrReader.IS_LOLLIPOP_OR_HIGHER) || !shapeModel.isRoundRect()) && isEnableCrop) {
            int saveCount = canvas.saveLayer(0, 0, rootView.getWidth(), rootView.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            if (drawCallback != null) drawCallback.drawCallback(canvas);

            paint.setXfermode(JFAttrReader.CLEAR_MODE);
            if (c) {
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
                canvas.drawPath(cornersMask, paint);
            }
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            if (drawCallback != null) drawCallback.drawCallback(canvas);
        }

        long end = System.currentTimeMillis();
        Log.d("耗时", "ViewEnhanceHelper draw:" + (end - start) + "ms");
    }

    public void updateCorners() {
        long start = System.currentTimeMillis();
        if (JFAttrReader.IS_LOLLIPOP_OR_HIGHER && (isEnableCrop || shadowCanvasEnable)) {
            rootView.setClipToOutline(true);
            rootView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (JFAttrReader.isShapeRect(shapeModel)) {
                        outline.setRect(0, 0, rootView.getWidth(), rootView.getHeight());
                    } else {
                        shadowDrawable.setBounds(0, 0, rootView.getWidth(), rootView.getHeight());
                        shadowDrawable.getOutline(outline);
                    }
                }
            });
        }

        boundsRect.set(0, 0, rootView.getWidth(), rootView.getHeight());
        if (isEnableCrop || shadowCanvasEnable) {
            shadowDrawable.getPathForSize(boundsRect, cornersMask);
        }
        long end = System.currentTimeMillis();
        Log.d("耗时", "计算path耗时:" + (end - start));
    }


    public interface DrawCallback {
        void drawCallback(Canvas canvas);
    }
}
