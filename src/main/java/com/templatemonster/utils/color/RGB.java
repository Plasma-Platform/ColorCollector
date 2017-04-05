package com.templatemonster.utils.color;

import java.io.Serializable;

/**
 * User: funduk
 * Date: Dec 10, 2006
 */
public class RGB implements Serializable {
    protected int r;
    protected int g;
    protected int b;
    protected int color;

    public RGB() {
    }

    public RGB(int color) {
        r = ((color & 0xFF0000) >> 16);
        g = ((color & 0xFF00) >> 8);
        b = (color & 0xFF);
        this.color = color & 0xFFFFFF;
    }

    public RGB(String colorStr) {
        int color = Integer.valueOf(colorStr, 16);

        r = ((color & 0xFF0000) >> 16);
        g = ((color & 0xFF00) >> 8);
        b = (color & 0xFF);
        this.color = color & 0xFFFFFF;
    }

    // from -441 to 441
    public int getDistance(RGB obj) {
        double dr = r - obj.r;
        double dg = g - obj.g;
        double db = b - obj.b;

        return (int) Math.sqrt(dr * dr + dg * dg + db * db);
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append(String.format("%02x", r));
        sb.append(String.format("%02x", g));
        sb.append(String.format("%02x", b));

        return sb.toString();
    }

    public static String toString(final int color) {

        int r = ((color & 0xFF0000) >> 16);
        int g = ((color & 0xFF00) >> 8);
        int b = (color & 0xFF);

        StringBuffer sb = new StringBuffer();

        sb.append(String.format("%02x", r));
        sb.append(String.format("%02x", g));
        sb.append(String.format("%02x", b));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RGB rgb = (RGB) o;

        if (color != rgb.color) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return color;
    }

    public static String getInvertedColor(final String hexColor) {
        String inv = Integer.toHexString(~Integer.parseInt(hexColor, 16) & 0xFFFFFF);
        int s = inv.length();

        switch (s) {
            case 1:
                inv += "00000";
                break;
            case 2:
                inv += "0000";
                break;
            case 3:
                inv += "000";
                break;
            case 4:
                inv += "00";
                break;
            case 5:
                inv += "0";
                break;
        }

        //fix against Shtirlitz effect
        if ("808080".equals(hexColor)) {
            inv = "ffffff";
        }

        return inv;
    }
}
