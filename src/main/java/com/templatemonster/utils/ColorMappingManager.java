package com.templatemonster.utils;

import com.opencsv.CSVReader;
import com.opencsv.ICSVParser;
import com.templatemonster.pojo.color.BigPaletteColorItem;
import com.templatemonster.pojo.color.ColorItem;
import com.templatemonster.pojo.color.ColorMapping;
import com.templatemonster.pojo.color.SmallPaletteColorItem;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColorMappingManager
{
    private Logger log = Logger.getLogger(getClass());

    private HashMap<Long, BigPaletteColorItem> bigPaletteColorItemList = null;
    private HashMap<Long, SmallPaletteColorItem> smallPaletteColorItemList = null;
    private HashMap<Long, ColorMapping> colorMappingList = null;


    /**
     * Get map big color item to id
     */
    public HashMap<Long, BigPaletteColorItem> getBigPaletteColorItemsMap() {
        if(null != bigPaletteColorItemList) {
            return bigPaletteColorItemList;
        }

        bigPaletteColorItemList = readColorItem("BigPaletteColorItem.csv", BigPaletteColorItem.class);

        return bigPaletteColorItemList;
    }

    /**
     * Get list big color items
     */
    public List<BigPaletteColorItem> getBigPaletteColorItems() {
        return new ArrayList<>(getBigPaletteColorItemsMap().values());
    }

    /**
     * Get map small color item to id
     */
    public HashMap<Long, SmallPaletteColorItem> getSmallPaletteColorItemsMap() {

        if(null != smallPaletteColorItemList) {
            return smallPaletteColorItemList;
        }

        smallPaletteColorItemList = readColorItem("SmallPaletteColorItem.csv", SmallPaletteColorItem.class);

        return smallPaletteColorItemList;
    }

    /**
     * Get list small color items
     */
    public List<SmallPaletteColorItem> getSmallPaletteColorItems() {
        return new ArrayList<>(getSmallPaletteColorItemsMap().values());
    }

    /**
     * Get map big color item to small color item
     */
    public HashMap<Long, ColorMapping> getColorMappingsMap() {

        if(null != colorMappingList) {
            return colorMappingList;
        }

        colorMappingList = new HashMap<>();

        try {

            FileReader f = new FileReader(getClass().getClassLoader().getResource("mapping/ColorMapping.csv").getFile());
            CSVReader reader = new CSVReader(f, ICSVParser.DEFAULT_SEPARATOR, ICSVParser.DEFAULT_QUOTE_CHARACTER, 1);

            String []line;
            while ((line = reader.readNext()) != null) {
                Long id = Long.valueOf(line[0]);
                Double weight = Double.valueOf(line[1]);
                Long smallColorItemId = Long.valueOf(line[2]);
                Long bigColorItemId = Long.valueOf(line[3]);

                BigPaletteColorItem bigPaletteColorItem = getBigPaletteColorItemsMap().get(bigColorItemId);
                SmallPaletteColorItem smallPaletteColorItem = getSmallPaletteColorItemsMap().get(smallColorItemId);

                colorMappingList.put(id, new ColorMapping(id, weight, bigPaletteColorItem, smallPaletteColorItem));
            }

        } catch(IOException | NullPointerException e) {
            log.error(e.getMessage());
        }

        return colorMappingList;
    }

    /**
     * Get list mapping big color item to small color item
     */
    public List<ColorMapping> getColorMappings() {
        return new ArrayList<>(getColorMappingsMap().values());
    }

    /**
     * Read color items from CSV file
     */
    private <T extends ColorItem> HashMap<Long, T> readColorItem(String file, Class<T> item) {

        HashMap<Long, T> list = new HashMap<>();

        try {

            FileReader f = new FileReader(getClass().getClassLoader().getResource("mapping/" + file).getFile());

            CSVReader reader = new CSVReader(f, ICSVParser.DEFAULT_SEPARATOR, ICSVParser.DEFAULT_QUOTE_CHARACTER, 1);

            String[] line;
            while ((line = reader.readNext()) != null) {
                Long id = Long.valueOf(line[0]);
                Integer color = Integer.valueOf(line[1]);

                list.put(id, item.getConstructor(Long.class, Integer.class).newInstance(id, color));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return list;
    }
}
