package com.templatemonster.utils.color;

public class RGBItemTmp extends RGBItem {
    /* Temporary class. For "Create colors" button only */

    private double k;

    public RGBItemTmp(int color) {
        super(color);
        k = 1;
    }

    public RGBItemTmp(int color, double k) {
        super(color);
        this.k = k;
    }

    public double getK() {
        return k;
    }
}
