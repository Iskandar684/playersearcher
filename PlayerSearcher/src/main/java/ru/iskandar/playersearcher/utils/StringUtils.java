package ru.iskandar.playersearcher.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static boolean isNullOrEmpty(String aString) {
        return aString == null || aString.isEmpty();
    }

}
