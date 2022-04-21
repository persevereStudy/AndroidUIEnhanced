package com.dz.dzbook.ui.view;


import com.dz.dzbook.ui.material.shadow.ShapeAppearanceModel;

public interface ShapeModelView {
    ShapeAppearanceModel getShapeModel();

    void setShapeModel(ShapeAppearanceModel model);

    void setCornerCut(float cornerCut);

    void setCornerRadii(
            float topLeftCornerRadius,
            float topRightCornerRadius,
            float bottomRightCornerRadius,
            float bottomLeftCornerRadius);

    void setCornerRadius(float cornerRadius);

    void setEnableCrop(boolean isEnableCrop);
}
