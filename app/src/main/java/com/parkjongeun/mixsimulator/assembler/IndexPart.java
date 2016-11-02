package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class IndexPart {

    public static boolean isIndexPart(String text, SymbolTable symbolTable) {

        // vacuous
        if ("".equals(text)) {
            return true;
        }

        // A comma followed by an expression
        if (text.length() >= 2 && text.charAt(0) == ',') {
            if (Expression.isExpression(text.substring(1), symbolTable)) {
                return true;
            }
        }

        return false;
    }
}
