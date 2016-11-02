package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class Symbol {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";

    private static final String REGEXP = "[A-Z0-9]*[A-Z][A-Z0-9]*";


    public static boolean isSymbol(String text) {
        final int length = text.length();

        if (length >= 1 && length <= 10) {
            // Filters
            for (int i = 0; i < length; ++i) {
                if (!isLetter(text.charAt(i))
                        && !isDigit(text.charAt(i))) {
                    return false;
                }
            }
            // Contains at least one letter?
            for (int i = 0; i < length; ++i) {
                if (isLetter(text.charAt(i)))
                    return true;
            }
        }
        return false;
    }

    private static boolean isLetter(char c) {
        final int size = LETTERS.length();
        for (int i = 0; i < size; ++i) {
            if (LETTERS.charAt(i) == c)
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
