package com.templatemonster.utils.color;

import com.templatemonster.pojo.color.BigPaletteColorItem;
import com.templatemonster.pojo.color.ColorMapping;
import com.templatemonster.pojo.color.SmallPaletteColorItem;
import com.templatemonster.utils.ColorMappingManager;
import com.templatemonster.utils.Conf;
import com.templatemonster.utils.ExecUtil;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RGBCollector
{

    private static Logger log = Logger.getLogger(RGBCollector.class);

    public HashMap<RGBItem, List<RGBItem>> collectFromURL(final String urlStr) throws Exception {

        File f = saveImageFile(urlStr);

        HashMap<RGBItem, List<RGBItem>> result = collect(f);

        if (!f.delete()) {
            System.out.println("RGBCollector(): can't delete temporary file, saved from URL");
        }

        return result;
    }

    private static File saveImageFile(String urlStr) throws Exception {
        URL url = new URL(urlStr);

        String formatName = url.getFile().substring(url.getFile().lastIndexOf(".") + 1);

        File file = File.createTempFile("img_from_url", "." + formatName);

        BufferedInputStream ustream = null;
        java.io.FileOutputStream fos = null;
        byte[] buffer = new byte[1024];

        try {

            ustream = new BufferedInputStream(url.openStream());

            fos = new java.io.FileOutputStream(file);

            int readBytes;
            do {
                readBytes = ustream.read(buffer, 0, buffer.length);

                if (readBytes >= 0) {
                    fos.write(buffer, 0, readBytes);
                }
            } while (readBytes >= 0);

            fos.flush();
            fos.close();
            ustream.close();

        } catch (Exception e) {

            if (null != fos) {
                fos.flush();
                fos.close();
            }

            if (null != ustream) {
                ustream.close();
            }

        }

        return file;
    }

    public HashMap<RGBItem, List<RGBItem>> collectFromFile(final String fileStr) throws Exception {
        File file = new File(fileStr);
        return collect(file);
    }

    /**
     * Uses a Runtime.exec()to use imagemagick to perform the given conversion
     * operation. Returns true on success, false on failure. Does not check if
     * either file exists.
     *
     * @param in  Description of the Parameter
     * @param out Description of the Parameter
     * @return Description of the Return Value
     */
    private static boolean convert(File in, File out) {

        String[] command = Conf.getImageConvertParams().split(" ");

        for (int i = 0; i < command.length; i++) {
            command[i] = command[i].trim();
            if ("{1}".equals(command[i])) {
                command[i] = in.getAbsolutePath();
            } else if ("{2}".equals(command[i])) {
                command[i] = out.getAbsolutePath();
            }
        }

        return ExecUtil.exec(command);
    }

    private HashMap<RGBItem, List<RGBItem>> collect(File file) throws Exception {
        File fileOut = convertFile(file);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage bi = ImageIO.read(fis);

        int[] pixels = parseImage(bi);

        if (!fileOut.delete()) {
            System.out.println("RGBCollector(): can't delete temporary file, converted from image");
        }

        return collect(pixels);
    }

    private static File convertFile(File fileIn) throws Exception {
        File fileOut = File.createTempFile(fileIn.getName(), "_out.png");
        convert(fileIn, fileOut);
        return fileOut;
    }

    private static int[] parseImage(BufferedImage bi) throws Exception {

        int w = bi.getWidth();
        int h = bi.getHeight();

        int[] pixels = new int[w * h];

        bi.getRGB(0, 0, w, h, pixels, 0, w);

        return pixels;
    }

    //-----------------------------------------------------------------------------------------------------------------

    private HashMap<RGBItem, List<RGBItem>> collect(final int[] imagePixels) throws Exception {

        long t1 = System.currentTimeMillis();

        ColorMappingManager colorMappingManager = new ColorMappingManager();

        List<BigPaletteColorItem> bigPaletteColorItems = colorMappingManager.getBigPaletteColorItems();

        List<ColorMapping> colorMappings = colorMappingManager.getColorMappings();

        List<RGB> bigPaletteColorItemsRgb = new ArrayList<>(bigPaletteColorItems.size());
        for (BigPaletteColorItem ci : bigPaletteColorItems) {
            bigPaletteColorItemsRgb.add(new RGB(ci.getColor()));
        }

        long t2 = System.currentTimeMillis();

        HashMap<Integer, Integer> pixelCounters = new HashMap<Integer, Integer>();
        for (int pixel : imagePixels) {
            Integer counter = pixelCounters.get(pixel);
            if (counter == null) {
                counter = 0;
            }
            pixelCounters.put(pixel, counter + 1);
        }

        /* For each pixel of image find nearest color of big palette. Then locate color of small palette by mapping and
increase its counter with weight value of mapping. */

        long tNearestSum = 0;
        HashMap<RGB, Integer> bigPaletteCounters = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : pixelCounters.entrySet()) {
            int pixel = entry.getKey();
            Integer pixelCounter = entry.getValue();
            long tNearest1 = System.currentTimeMillis();
            RGB nearestColor = findNearestColorInBigPalette(pixel, bigPaletteColorItemsRgb);
            tNearestSum += System.currentTimeMillis() - tNearest1;

            if (nearestColor != null) {
                Integer counter = bigPaletteCounters.get(nearestColor);
                if (counter == null) {
                    counter = 0;
                }
                bigPaletteCounters.put(nearestColor, counter + pixelCounter);
            } else {
                System.out.println("collect(): nearestColor is null");
            }
        }

        long t3 = System.currentTimeMillis();

        /* Small palette color, related big palette colors */
        HashMap<RGBItem, List<RGBItem>> palette = new HashMap<RGBItem, List<RGBItem>>();
        for (ColorMapping cm : colorMappings) {

            BigPaletteColorItem bigColor = cm.getBigPaletteColorItem();
            SmallPaletteColorItem smallColor = cm.getSmallPaletteColorItem();

            Integer pixels = bigPaletteCounters.get(new RGB(bigColor.getColor()));

            if (pixels == null) {
                // big part of mapping not present in big palette
                continue;
            }

            RGBItem rgbSmall = null;
            for (RGBItem rgb : palette.keySet()) {
                if (rgb.getColor() == smallColor.getColor()) {
                    rgbSmall = rgb;
                    break;
                }
            }

            if (rgbSmall == null) {
                rgbSmall = new RGBItem(smallColor.getColor());
                palette.put(rgbSmall, new ArrayList<RGBItem>());
            }

            double pixelsWithWeights = cm.getWeight() * pixels;
            rgbSmall.setPixels(rgbSmall.getPixels() + pixels);
            rgbSmall.setPixelsWithWeights(rgbSmall.getPixelsWithWeights() + pixelsWithWeights);

            List<RGBItem> relatedBigPaletteColors = palette.get(rgbSmall);
            RGBItem rgbBig = new RGBItem(bigColor.getColor());
            rgbBig.setPixels(rgbBig.getPixels() + pixels);
            rgbBig.setPixelsWithWeights(rgbBig.getPixelsWithWeights() + pixelsWithWeights);
            relatedBigPaletteColors.add(rgbBig);
        }

        double pixelsWithWeightsAllBySmallPalette = 0;
        for (Map.Entry<RGBItem, List<RGBItem>> entry : palette.entrySet()) {
            RGBItem smallColor = entry.getKey();
            pixelsWithWeightsAllBySmallPalette += smallColor.getPixelsWithWeights();
        }

        final int imagePixelsLength = imagePixels.length;

        /* Calculate percents by counters */
        for (Map.Entry<RGBItem, List<RGBItem>> entry : palette.entrySet()) {
            RGBItem small = entry.getKey();
            List<RGBItem> bigs = entry.getValue();

            small.setPercentsOfPixels(100.0 * small.getPixels() / imagePixelsLength);
            small.setPercentsOfPixelsWithWeightDivPixels(100.0 * small.getPixelsWithWeights() / imagePixelsLength);
            small.setPercentsOfPixelsWithWeight(100.0 * small.getPixelsWithWeights() / pixelsWithWeightsAllBySmallPalette);

            for (RGBItem big : bigs) {
                big.setPercentsOfPixels(100.0 * big.getPixels() / imagePixelsLength);
                big.setPercentsOfPixelsWithWeightDivPixels(100.0 * big.getPixelsWithWeights() / imagePixelsLength);
                big.setPercentsOfPixelsWithWeight(100.0 * big.getPixelsWithWeights() / pixelsWithWeightsAllBySmallPalette);
            }
        }

        long t4 = System.currentTimeMillis();

        System.out.println("Collect colors: " + (t4 - t1) + "msec (" + (t2 - t1) + "+" + (t3 - t2) + "+" + (t4 - t3) + "; tNearestSum=" + tNearestSum + ")");
        System.out.println("All pixels " + imagePixels.length + " Unique pixels " + pixelCounters.size() + " Compress ratio " + Math.round((1 - 1d * pixelCounters.size() / imagePixels.length) * 100) + "%");

        return palette;
    }

    private static RGB findNearestColorInBigPalette(int pixel, List<RGB> bigPaletteColorItemsRgb) {

        RGB color = new RGB(pixel);

        RGB nearestColor = null;

        int minimalDifference = Integer.MAX_VALUE;
        for (RGB current : bigPaletteColorItemsRgb) {
            int difference = current.getDistance(color);
            if (difference < minimalDifference) {
                minimalDifference = difference;
                nearestColor = current;
            }
        }

        return nearestColor;
    }
}
