package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class IndexPart {

    public static boolean isIndexPart(String text, SymbolTable symbolTable) {

        // vacuous
        if (isVacuous(text)) {
            return true;
        }

        // A comma followed by an expression
        if (isCommaFollowedByExprForm(text, symbolTable)) {
            return true;
        }
        return false;
    }

    private static boolean isVacuous(String text) {
        return "".equals(text);
    }

    private static boolean isCommaFollowedByExprForm(String text, SymbolTable symTable) {
        if (text.length() >= 2 && text.charAt(0) == ',') {
            if (Expression.isExpression(text.substring(1), symTable)) {
                return true;
            }
        }
        return false;
    }

    // CAUTION: Assumes that isCommaFollowedByExprForm(text) == true.
    private static String extractExprWeak(String text) {
        //if (isCommaFollowedByExprForm(text, symTable)) {
        return text.substring(1);
        //}
        //throw new IllegalArgumentException("arg1: " + text);
    }

    public static int eval(String text, SymbolTable symTable, int locCounter) {
        if (isVacuous(text)) {
            return 0;
        } else if (isCommaFollowedByExprForm(text, symTable)) {
            String expr = extractExprWeak(text);
            return Expression.eval(expr, symTable, locCounter);
        }
        throw new IllegalArgumentException("text: " + text);
    }
}
