package com.dz.dzbook.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.view.View;

import com.dz.dzbook.R;
import com.dz.dzbook.ui.material.animator.AnimatedColorStateList;
import com.dz.dzbook.ui.material.drawable.drawable.AlphaDrawable;
import com.dz.dzbook.ui.material.shadow.CutCornerTreatment;
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
 */
public class JFAttrReader {

    public static final boolean IS_LOLLIPOP_OR_HIGHER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public static final boolean IS_PIE_OR_HIGHER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;

    public static PorterDuffXfermode CLEAR_MODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    //阴影复用
    public static NinePatchDrawable shadowDrawable;

    public static Drawable getShadowDrawable(Context context) {
        if (shadowDrawable == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_shadow);
            shadowDrawable = new NinePatchDrawable(context.getResources(), bitmap, bitmap.getNinePatchChunk(), new Rect(), "");
        }
        return shadowDrawable;
    }

    public static boolean isShapeRect(ShapeAppearanceModel model) {
        return model.getTopLeftCorner().getCornerSize() <= 0.2f &&
                model.getTopRightCorner().getCornerSize() <= 0.2f &&
                model.getBottomLeftCorner().getCornerSize() <= 0.2f &&
                model.getBottomRightCorner().getCornerSize() <= 0.2f;
    }

    public static int getDrawableAlpha(Drawable background) {
        if (background == null)
            return 255;
        background = background.getCurrent();
        if (background instanceof ColorDrawable)
            return ((ColorDrawable) background).getAlpha();
        if (background instanceof AlphaDrawable)
            return ((AlphaDrawable) background).getAlpha();
        return 255;
    }

    public static void initCornerCutRadius(ShapeModelView shapeModelView, TypedArray a, int[] ids) {
        int jf_cornerRadiusTopStart = ids[0];
        int jf_cornerRadiusTopEnd = ids[1];
        int jf_cornerRadiusBottomStart = ids[2];
        int jf_cornerRadiusBottomEnd = ids[3];
        int jf_cornerRadius = ids[4];
        int jf_cornerCutTopStart = ids[5];
        int jf_cornerCutTopEnd = ids[6];
        int jf_cornerCutBottomStart = ids[7];
        int jf_cornerCutBottomEnd = ids[8];
        int jf_cornerCut = ids[9];
        int jf_crop_enable = ids[10];

        ShapeAppearanceModel model = shapeModelView.getShapeModel();
        float cornerRadius = Math.max(a.getDimension(jf_cornerRadius, 0), 0.1f);
        float cornerRadiusTopStart = a.getDimension(jf_cornerRadiusTopStart, cornerRadius);
        float cornerRadiusTopEnd = a.getDimension(jf_cornerRadiusTopEnd, cornerRadius);
        float cornerRadiusBottomStart = a.getDimension(jf_cornerRadiusBottomStart, cornerRadius);
        float cornerRadiusBottomEnd = a.getDimension(jf_cornerRadiusBottomEnd, cornerRadius);
        float cornerCut = a.getDimension(jf_cornerCut, 0);
        float cornerCutTopStart = a.getDimension(jf_cornerCutTopStart, cornerCut);
        float cornerCutTopEnd = a.getDimension(jf_cornerCutTopEnd, cornerCut);
        float cornerCutBottomStart = a.getDimension(jf_cornerCutBottomStart, cornerCut);
        float cornerCutBottomEnd = a.getDimension(jf_cornerCutBottomEnd, cornerCut);
        boolean cropEnable = a.getBoolean(jf_crop_enable, false);
        model.setTopLeftCorner(cornerCutTopStart >= cornerRadiusTopStart ? new CutCornerTreatment(cornerCutTopStart) : new RoundedCornerTreatment(cornerRadiusTopStart));
        model.setTopRightCorner(cornerCutTopEnd >= cornerRadiusTopEnd ? new CutCornerTreatment(cornerCutTopEnd) : new RoundedCornerTreatment(cornerRadiusTopEnd));
        model.setBottomLeftCorner(cornerCutBottomStart >= cornerRadiusBottomStart ? new CutCornerTreatment(cornerCutBottomStart) : new RoundedCornerTreatment(cornerRadiusBottomStart));
        model.setBottomRightCorner(cornerCutBottomEnd >= cornerRadiusBottomEnd ? new CutCornerTreatment(cornerCutBottomEnd) : new RoundedCornerTreatment(cornerRadiusBottomEnd));
        shapeModelView.setShapeModel(model);
        shapeModelView.setEnableCrop(cropEnable);
    }

    public static void initElevation(ShadowView view, TypedArray a, int[] ids) {
        int jf_elevation = ids[0];
        int jf_shadowColor = ids[1];
        int jf_ambientShadowColor = ids[2];
        int jf_spotShadowColor = ids[3];
        int jf_shadowCanvasEnable = ids[4];

        float elevation = a.getDimension(jf_elevation, 0);
        ColorStateList shadowColor = a.getColorStateList(jf_shadowColor);
        view.setElevationShadowColor(shadowColor != null ? shadowColor.withAlpha(255) : null);
        if (a.hasValue(jf_ambientShadowColor)) {
            ColorStateList ambientShadowColor = a.getColorStateList(jf_ambientShadowColor);
            view.setOutlineAmbientShadowColor(ambientShadowColor != null ? ambientShadowColor.withAlpha(255) : null);
        }
        if (a.hasValue(jf_spotShadowColor)) {
            ColorStateList spotShadowColor = a.getColorStateList(jf_spotShadowColor);
            view.setOutlineSpotShadowColor(spotShadowColor != null ? spotShadowColor.withAlpha(255) : null);
        }
        boolean shadowCanvasEnable = a.getBoolean(jf_shadowCanvasEnable,false);
        view.setShadowCanvasEnable(shadowCanvasEnable);
        view.setElevation(elevation);
    }

    public static void initStroke(StrokeView strokeView, TypedArray a, int[] ids) {
        int jf_stroke = ids[0];
        int jf_strokeWidth = ids[1];

        View view = (View) strokeView;
//        ColorStateList color = getDefaultColorStateList(view, a, jf_stroke);
        ColorStateList color = null;
        if (color == null)
            color = a.getColorStateList(jf_stroke);

        if (color != null)
            strokeView.setStroke(AnimatedColorStateList.fromList(color, animation -> view.postInvalidate()));
        strokeView.setStrokeWidth(a.getDimension(jf_strokeWidth, 0));
    }

    public static void initGradient(GradientView gradientView, TypedArray a, int[] ids) {
        int jf_start_color = ids[0];
        int jf_end_color = ids[1];
        int jf_color_orientation = ids[2];
        int jf_solid_color = ids[3];

        int startColor = a.getColor(jf_start_color, Color.TRANSPARENT);
        int endColor = a.getColor(jf_end_color, Color.TRANSPARENT);
        int colorOrientation = a.getInt(jf_color_orientation, GradientOrientation.NONE);
        int solidColor = a.getColor(jf_solid_color, Color.TRANSPARENT);
        gradientView.setSolidColor(solidColor);
        gradientView.setGradientOrientation(colorOrientation);
        gradientView.setGradientColor(startColor, endColor);
    }


}
