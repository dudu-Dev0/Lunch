package com.xtc.utils;

import android.content.Context;

public class DensityUtil {
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getDensity(Context context, float f) {
        return (int) ((getDensity(context) * f) + 0.5f);
    }

    public static float b(Context context, float f) {
        return getDensity(context) * f;
    }

    public static int c(Context context, float f) {
        return (int) (f / getDensity(context));
    }

    public static int d(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int e(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}