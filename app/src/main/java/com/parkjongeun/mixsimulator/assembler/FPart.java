package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class FPart {

    public static boolean isFPart(String text, SymbolTable symbolTable) {

        // vacuous
        if ("".equals(text)) {
            return true;
        }

        // ( expr )
        if (text.length() >= 3
                && text.charAt(0) == '('
                && text.charAt(text.length()-1) == ')') {
            String expr = text.substring(1, text.length()-1);

            if (Expression.isExpression(expr, symbolTable)) {
                return true;
            }
        }

        return false;
    }
}
