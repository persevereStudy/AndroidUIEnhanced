package com.dz.dzbook.ui.view;

import android.graphics.drawable.GradientDrawable;

/**
 * @author wj
 *  * Date 2022/4/18 3:46 下午
 */
public class GradientOrientation {

    public static final int NONE = -1; //没有渐变
    public static final int LEFT_TO_RIGHT = 0; //左到右
    public static final int RIGHT_TO_LEFT = 1; //右到左
    public static final int TOP_TO_BOTTOM = 2; //上到下
    public static final int BOTTOM_TO_TOP = 3; //下到上
    public static final int TL_TO_BR = 4; // 左上到
    public static final int TR_TO_BL = 5; // 右上到
    public static final int BR_TO_TL = 6; // 右下到
    public static final int BL_TO_TR = 7; // 左下到

    public static GradientDrawable.Orientation getGradientOrientation(int colorOrientation) {
        switch (colorOrientation) {
            case LEFT_TO_RIGHT:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case RIGHT_TO_LEFT:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case TOP_TO_BOTTOM:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case BOTTOM_TO_TOP:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case TL_TO_BR:
                return GradientDrawable.Orientation.TL_BR;
            case TR_TO_BL:
                return GradientDrawable.Orientation.TR_BL;
            case BR_TO_TL:
                return GradientDrawable.Orientation.BR_TL;
            case BL_TO_TR:
                return GradientDrawable.Orientation.BL_TR;
            default:
                return GradientDrawable.Orientation.LEFT_RIGHT;
        }
    }
}
