package com.xtc.utils;

import android.graphics.Path;

import java.util.HashMap;

public class SuperEllipsePathUtil {
    private static final HashMap<SuperEllipsePathEntity, Path> pathMap = new HashMap<>();

    private static double sgn(double d) {
        if (d > 0.0d) {
            return 1.0d;
        }
        return d < 0.0d ? -1.0d : 0.0d;
    }

    public static Path getSuperEllipsePath(int i, int i2, float f) {
        SuperEllipsePathEntity superEllipsePathEntity = new SuperEllipsePathEntity(i, i2, f);
        Path path = pathMap.get(superEllipsePathEntity);
        if (path != null) {
            return path;
        }
        Path path2 = new Path();
        addSuperEllipseToPath(path2, i, i2, f);
        pathMap.put(superEllipsePathEntity, path2);
        return path2;
    }

    private static void addSuperEllipseToPath(Path path, int i, int i2, float f) {
        double d = 0.0d;
        for (int i3 = 0; i3 < 360; i3++) {
            double radians = Math.toRadians(d);
            float x = getX(i, radians, f);
            float y = getY(i2, radians, f);
            if (i3 == 0) {
                path.moveTo(x, y);
            }
            d += 1.0d;
            double radians2 = Math.toRadians(d);
            path.lineTo(getX(i, radians2, f), getY(i2, radians2, f));
        }
        path.close();
    }

    private static float getX(int i, double d, float f) {
        double pow = Math.pow(Math.abs(Math.cos(d)), f);
        return (float) (pow * (double) i * sgn(Math.cos(d)));
    }

    private static float getY(int i, double d, float f) {
        double pow = Math.pow(Math.abs(Math.sin(d)), f);
        return (float) (pow * (double) i * sgn(Math.sin(d)));
    }

    /* loaded from: classes2.dex */
    static class SuperEllipsePathEntity {
        float corners;
        int radX;
        int radY;

        public SuperEllipsePathEntity(int i, int i2, float f) {
            this.radX = i;
            this.radY = i2;
            this.corners = f;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                SuperEllipsePathEntity superEllipsePathEntity = (SuperEllipsePathEntity) obj;
                return this.radX == superEllipsePathEntity.radX && this.radY == superEllipsePathEntity.radY && this.corners == superEllipsePathEntity.corners;
            }
            return false;
        }

        public int hashCode() {
            return ((int) this.corners) + 1 + this.radY + this.radX;
        }
    }
}