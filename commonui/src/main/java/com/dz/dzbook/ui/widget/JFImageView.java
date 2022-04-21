package com.dz.dzbook.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dz.dzbook.R;
import com.dz.dzbook.ui.JFAttrReader;
import com.dz.dzbook.ui.material.shadow.ShapeAppearanceModel;
import com.dz.dzbook.ui.view.GradientView;
import com.dz.dzbook.ui.view.ShadowView;
import com.dz.dzbook.ui.view.ShapeModelView;
import com.dz.dzbook.ui.view.StrokeView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author wj
 * Date 2022/4/18 3:46 下午
 * ImageView增强
 */
public class JFImageView extends AppCompatImageView implements
        ShapeModelView,
        ShadowView,
        StrokeView,
        GradientView {

    private ViewEnhanceHelper viewEnhanceHelper;

    public JFImageView(Context context) {
        this(context, null);
    }

    public JFImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JFImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    protected void init(AttributeSet attrs, int defStyleAttr) {
        viewEnhanceHelper = new ViewEnhanceHelper(getContext(), this);
        viewEnhanceHelper.readAttrs(attrs, R.styleable.JFImageView, defStyleAttr);
    }

    @Override
    public void setSolidColor(int solidColor) {
        viewEnhanceHelper.setSolidColor(solidColor);
    }

    @Override
    public void setGradientColor(int startColor, int endColor) {
        viewEnhanceHelper.setGradientColor(startColor, endColor);
    }

    @Override
    public int[] getGradientColor() {
        return viewEnhanceHelper.getGradientColor();
    }

    @Override
    public void setGradientOrientation(int orientation) {
        viewEnhanceHelper.setGradientOrientation(orientation);
    }

    @Override
    public int getGradientOrientation() {
        return viewEnhanceHelper.getGradientOrientation();
    }

    @Override
    public boolean hasShadow() {
        return viewEnhanceHelper.hasShadow();
    }

    @Override
    public void drawShadow(Canvas canvas) {
        viewEnhanceHelper.drawShadow(canvas);
    }

    @Override
    public float getElevation() {
        return viewEnhanceHelper.elevation;
    }

    @Override
    public void setElevation(float elevation) {
        if (JFAttrReader.IS_PIE_OR_HIGHER) {
            super.setElevation(elevation);
            super.setTranslationZ(viewEnhanceHelper.translationZ);
        } else if (JFAttrReader.IS_LOLLIPOP_OR_HIGHER) {
            if (viewEnhanceHelper.ambientShadowColor == null || viewEnhanceHelper.spotShadowColor == null) {
                super.setElevation(elevation);
                super.setTranslationZ(viewEnhanceHelper.translationZ);
            } else {
                super.setElevation(0);
                super.setTranslationZ(0);
            }
        } else if (elevation != viewEnhanceHelper.elevation && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        viewEnhanceHelper.setElevation(elevation);
    }

    @Override
    public float getTranslationZ() {
        return viewEnhanceHelper.translationZ;
    }

    @Override
    public void setTranslationZ(float translationZ) {
        if (translationZ == viewEnhanceHelper.translationZ)
            return;
        if (JFAttrReader.IS_PIE_OR_HIGHER) {
            super.setTranslationZ(translationZ);
        } else if (JFAttrReader.IS_LOLLIPOP_OR_HIGHER) {
            if (viewEnhanceHelper.ambientShadowColor == null || viewEnhanceHelper.spotShadowColor == null) {
                super.setTranslationZ(translationZ);
            } else {
                super.setTranslationZ(0);
            }
        } else if (translationZ != viewEnhanceHelper.translationZ && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        viewEnhanceHelper.setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        viewEnhanceHelper.setElevationShadowColor(shadowColor);
    }

    @Override
    public void setElevationShadowColor(int color) {
        viewEnhanceHelper.setElevationShadowColor(color);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return viewEnhanceHelper.getElevationShadowColor();
    }

    @Override
    public void setOutlineAmbientShadowColor(ColorStateList color) {
        viewEnhanceHelper.setOutlineAmbientShadowColor(color);
        if (JFAttrReader.IS_PIE_OR_HIGHER) {
            super.setOutlineAmbientShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            setElevation(viewEnhanceHelper.elevation);
            setTranslationZ(viewEnhanceHelper.translationZ);
        }
    }

    @Override
    public void setOutlineSpotShadowColor(ColorStateList color) {
        viewEnhanceHelper.setOutlineAmbientShadowColor(color);
        if (JFAttrReader.IS_PIE_OR_HIGHER) {
            super.setOutlineSpotShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            setElevation(viewEnhanceHelper.elevation);
            setTranslationZ(viewEnhanceHelper.translationZ);
        }
    }

    @Override
    public void setShadowCanvasEnable(boolean shadowCanvasEnable) {
        viewEnhanceHelper.setShadowCanvasEnable(shadowCanvasEnable);
    }

    @Override
    public ShapeAppearanceModel getShapeModel() {
        return viewEnhanceHelper.getShapeModel();
    }

    @Override
    public void setShapeModel(ShapeAppearanceModel model) {
        viewEnhanceHelper.setShapeModel(model);
    }

    @Override
    public void setCornerCut(float cornerCut) {
        viewEnhanceHelper.setCornerCut(cornerCut);
    }

    @Override
    public void setCornerRadii(float topLeftCornerRadius, float topRightCornerRadius, float bottomRightCornerRadius, float bottomLeftCornerRadius) {
        viewEnhanceHelper.setCornerRadii(topLeftCornerRadius, topRightCornerRadius, bottomRightCornerRadius, bottomLeftCornerRadius);
    }

    @Override
    public void setCornerRadius(float cornerRadius) {
        viewEnhanceHelper.setCornerRadius(cornerRadius);
    }

    @Override
    public void setEnableCrop(boolean isEnableCrop) {
        viewEnhanceHelper.setEnableCrop(isEnableCrop);
    }

    @Override
    public void setStroke(ColorStateList colorStateList) {
        viewEnhanceHelper.setStroke(colorStateList);
    }

    @Override
    public void setStroke(int color) {
        viewEnhanceHelper.setStroke(color);
    }

    @Override
    public ColorStateList getStroke() {
        return viewEnhanceHelper.getStroke();
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        viewEnhanceHelper.setStrokeWidth(strokeWidth);
    }

    @Override
    public float getStrokeWidth() {
        return viewEnhanceHelper.getStrokeWidth();
    }

    //draw
    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(@NonNull Canvas canvas) {
        long start = System.currentTimeMillis();

        if (JFAttrReader.IS_PIE_OR_HIGHER) {
            if (viewEnhanceHelper.spotShadowColor != null)
                super.setOutlineSpotShadowColor(viewEnhanceHelper.spotShadowColor.getColorForState(getDrawableState(), viewEnhanceHelper.spotShadowColor.getDefaultColor()));
            if (viewEnhanceHelper.ambientShadowColor != null)
                super.setOutlineAmbientShadowColor(viewEnhanceHelper.ambientShadowColor.getColorForState(getDrawableState(), viewEnhanceHelper.ambientShadowColor.getDefaultColor()));
        }
        viewEnhanceHelper.draw(canvas, new ViewEnhanceHelper.DrawCallback() {
            @Override
            public void drawCallback(Canvas canvas) {
                drawInternal(canvas);
            }
        });

        long end = System.currentTimeMillis();
        Log.d("耗时", "View draw:" + (end - start) + "ms");
    }

    public void drawInternal(@NonNull Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        viewEnhanceHelper.updateCorners();
    }
}

