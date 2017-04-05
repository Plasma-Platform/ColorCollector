package com.templatemonster.utils.color;

public class RGBItem extends RGB implements Comparable {

    private int pixels;  // Number of pixels of this color in picture
    private double pixelsWithWeights;  // Number of pixels of this color in picture * mapping
    private double percentsOfPixels;  // pixels / (pixels for color1 + pixels for color2 ...)
    private double percentsOfPixelsWithWeightDivPixels;  // pixelsWithWeights / (pixels for color1 + pixels for color2 ...)
    private double percentsOfPixelsWithWeight;  // pixelsWithWeights / (pixelsWithWeights for color1 + pixelsWithWeights for color2 ...)

    public RGBItem(int color) {
        super(color);
        pixels = 0;
        pixelsWithWeights = 0;
        percentsOfPixels = 0;
        percentsOfPixelsWithWeightDivPixels = 0;
        percentsOfPixelsWithWeight = 0;
    }

    public int getColor() {
        return color;
    }

    public int getPixels() {
        return pixels;
    }

    public void setPixels(int pixels) {
        this.pixels = pixels;
    }

    public double getPixelsWithWeights() {
        return pixelsWithWeights;
    }

    public void setPixelsWithWeights(double pixelsWithWeights) {
        this.pixelsWithWeights = pixelsWithWeights;
    }

    public double getPercentsOfPixels() {
        return percentsOfPixels;
    }

    public void setPercentsOfPixels(double percentsOfPixels) {
        this.percentsOfPixels = percentsOfPixels;
    }

    public double getPercentsOfPixelsWithWeightDivPixels() {
        return percentsOfPixelsWithWeightDivPixels;
    }

    public void setPercentsOfPixelsWithWeightDivPixels(double percentsOfPixelsWithWeightDivPixels) {
        this.percentsOfPixelsWithWeightDivPixels = percentsOfPixelsWithWeightDivPixels;
    }

    public double getPercentsOfPixelsWithWeight() {
        return percentsOfPixelsWithWeight;
    }

    public void setPercentsOfPixelsWithWeight(double percentsOfPixelsWithWeight) {
        this.percentsOfPixelsWithWeight = percentsOfPixelsWithWeight;
    }

    public int compareTo(Object o) {
        RGBItem item = (RGBItem) o;
        return (int) (Math.round(this.percentsOfPixelsWithWeightDivPixels * 1000) - Math.round(item.percentsOfPixelsWithWeightDivPixels * 1000));
        /* NOTE: round(0.5) - round(0.4) is 1, but round(0.5 - 0.4) is 0 */
    }

}
