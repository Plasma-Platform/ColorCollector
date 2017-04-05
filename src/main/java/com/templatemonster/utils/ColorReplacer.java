package com.templatemonster.utils;

import java.util.*;

public class ColorReplacer
{

    public static final Map<String, String> colorToNameMap = Collections.synchronizedMap(new LinkedHashMap<String, String>());

    private static final Map<String, String> nameToColorMap = Collections.synchronizedMap(new LinkedHashMap<String, String>());

    static {
        colorToNameMap.put("ff0000", "red");
        colorToNameMap.put("ff7f00", "orange");
        colorToNameMap.put("ffff00", "yellow");
        colorToNameMap.put("00ff00", "green");
        colorToNameMap.put("00ffff", "cyan");
        colorToNameMap.put("0000ff", "blue");
        colorToNameMap.put("ee82ee", "pink");
        colorToNameMap.put("8b00ff", "violet");
        colorToNameMap.put("964b00", "brown");
        colorToNameMap.put("808080", "grey");
        colorToNameMap.put("ffffff", "white");
        colorToNameMap.put("000000", "black");

        /* Fill reverted map */
        for (Map.Entry<String, String> entry : colorToNameMap.entrySet()) {
            nameToColorMap.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * @return array of string. array[0] -- new keyword string, array[1] -- new colors string
     */
    public static String[] process(String keyword, String colors) {
        if (null == keyword) {
            return new String[]{keyword, colors};
        }
        if (null == colors) {
            colors = "";
        }

        StringBuffer colorsSB = new StringBuffer(colors);
        StringBuffer keywordSB = new StringBuffer();

        List<String> keywordListed = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(keyword, " ,.;:\n\t", true);
        while (st.hasMoreTokens()) {
            keywordListed.add(st.nextToken());
        }

        boolean isColor;
        for (String k : keywordListed) {
            isColor = false;
            for (Map.Entry<String, String> entry : nameToColorMap.entrySet()) {
                if (entry.getKey().equals(k) || entry.getValue().equals(k)) {
                    if (colorsSB.length() > 0) {
                        colorsSB.append(",");
                    }
                    colorsSB.append(entry.getValue());
                    isColor = true;
                    break;
                }
            }
            if (!isColor) {
                keywordSB.append(k);
            }
        }

        if (colors.length() > 0) {
            colors += ",";
        }

        String resKeyword = keywordSB.toString();
        String resColors = colors + colorsSB.toString();

        resColors = removeDuplicates(resColors);

        return new String[]{resKeyword, resColors};
    }

    private static String removeDuplicates(String strIn) {
        if (null == strIn) {
            return null;
        }
        String[] ss = strIn.split(",");
        HashMap<String, String> hm = new HashMap<String, String>();
        for (String s : ss) {
            hm.put(s, s);
        }
        StringBuffer sb = new StringBuffer();
        for (String s : hm.keySet()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(s);
        }
        return sb.toString();
    }

}
