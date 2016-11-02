package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

// Word Value
public class WValue {

    public static boolean isWValue(String text, SymbolTable symbolTable) {
        int commaIndex = text.lastIndexOf(',');
        if (commaIndex != -1) {
            if (commaIndex < text.length() - 1) {
                if (isExprFPart(text.substring(commaIndex + 1), symbolTable)) {
                    if (isWValue(text.substring(0, commaIndex), symbolTable)) {
                        return true;
                    }
                }
            }
        } else {
            if (isExprFPart(text, symbolTable)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isExprFPart(String text, SymbolTable symbolTable) {
        String expr;
        String fpart;

        final int lParenIndex = text.indexOf('(');
        if (lParenIndex != -1) {
            expr = text.substring(0, lParenIndex);
            fpart = text.substring(lParenIndex);
        } else {
            expr = text;
            fpart = "";
        }

        if (Expression.isExpression(expr, symbolTable)) {
            if (FPart.isFPart(fpart, symbolTable)) {
                return true;
            }
        }
        return false;
    }

}
