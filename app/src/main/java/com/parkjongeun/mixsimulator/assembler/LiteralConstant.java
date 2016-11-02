package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class LiteralConstant {

    public static boolean isLiteralContant(String text, SymbolTable symbolTable) {
        if (text.length() >= 3) {
            if (text.charAt(0) == '=') {
                if (text.charAt(text.length()-1) == '=') {
                    String wvalue = text.substring(1, text.length()-1);
                    if (WValue.isWValue(wvalue, symbolTable)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
