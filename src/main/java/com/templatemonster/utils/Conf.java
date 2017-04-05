package com.templatemonster.utils;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public final class Conf
{

    private static ResourceBundle getProps() {
        return ResourceBundle.getBundle("conf");
    }

    private  static String getter(String propertyName, String defaultValue) {
        String property = getProps().getString(propertyName);
        if (property.isEmpty()) {
            Logger log = Logger.getLogger(Conf.class);
            String error = "Property " + propertyName + " undefined";
            log.error(error);
            //throw new Exception(error);
            return defaultValue; 
        }
        return property.trim();
    }

    private static int getterInt(String propertyName, int defaultValue) {
        String t = getter(propertyName, String.valueOf(defaultValue));

        try {
            return Integer.valueOf(t);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }

    }

    public static String getSecretWord() {
        return getter("SECRET_WORD", "");
    }
    public static String getImageConvertParams() {
        return getter("IMAGE_CONVERT_PARAMS", "convert -map netscape: {1} {2}");
    }
}