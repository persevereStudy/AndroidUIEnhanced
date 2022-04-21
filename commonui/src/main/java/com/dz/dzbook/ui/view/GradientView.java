package com.dz.dzbook.ui.view;

/**
 * @author wj
 *  * Date 2022/4/18 3:46 下午
 * 渐变View
 */
public interface GradientView {

    void setSolidColor(int solidColor);

    int getSolidColor();

    void setGradientColor(int startColor, int endColor);

    int[] getGradientColor();

    void setGradientOrientation(int orientation);

    int getGradientOrientation();
}
