package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class Number {
    private static final String DIGITS = "0123456789";


    public static boolean isNumber(String text) {
        final int length = text.length();

        // A number is a string of one to ten digits.
        if (length >= 1 && length <= 10) {
            // Filters
            for (int i = 0; i < length; ++i) {
                if (!isDigit(text.charAt(i)))
                    return false;
            }
            return true;
        }
        return false;
    }

    private static boolean isDigit(char c) {
        final int size = DIGITS.length();
        for (int i = 0; i < size; ++i) {
            if (DIGITS.charAt(i) == c)
                return true;
        }
        return false;
    }
}
