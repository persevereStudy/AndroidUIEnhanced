package com.dz.dzbook.ui.material.shadow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

/**
 * CornerFamily enum that holds which family to be used to create a {@link CornerTreatment}
 *
 * <p>The corner family determines which family to use to create a {@link CornerTreatment}. Setting
 * the CornerFamily to {@link CornerFamily#ROUNDED} sets the corner treatment to {@link
 * RoundedCornerTreatment}, and setting the CornerFamily to {@link CornerFamily#CUT} sets the corner
 * treatment to a {@link CutCornerTreatment}.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@IntDef({CornerFamily.ROUNDED, CornerFamily.CUT})
@Retention(RetentionPolicy.SOURCE)
public @interface CornerFamily {
    /** Corresponds to a {@link RoundedCornerTreatment}. */
    int ROUNDED = 0;
    /** Corresponds to a {@link CutCornerTreatment}. */
    int CUT = 1;
}