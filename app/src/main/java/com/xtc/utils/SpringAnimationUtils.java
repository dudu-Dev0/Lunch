package com.xtc.utils;

import android.view.View;

import androidx.dynamicanimation.animation.FloatPropertyCompat;

public class SpringAnimationUtils {
    public static final FloatPropertyCompat<View> FLOAT_PROPERTY_TRANSLATION_Y = new FloatPropertyCompat<>("translationY") { // from class: com.xtc.ui.widget.util.SpringAnimationUtils.1
        @Override // android.support.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationY();
        }

        @Override // android.support.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }
    };
}