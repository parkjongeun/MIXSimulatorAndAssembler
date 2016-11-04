package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class FPart {

    public static boolean isFPart(String text, SymbolTable symbolTable) {

        // vacuous
        if (isVacuous(text)) {
            return true;
        }

        // ( expr )
        if (isFormOfLParenExprRParen(text, symbolTable)) {
            return true;
        }

        return false;
    }

    private static boolean isVacuous(String text) {
        return "".equals(text);
    }

    private static boolean isFormOfLParenExprRParen(String text, SymbolTable symTable) {
        if (text.length() >= 3
                && text.charAt(0) == '('
                && text.charAt(text.length()-1) == ')') {
            String expr = text.substring(1, text.length()-1);

            if (Expression.isExpression(expr, symTable)) {
                return true;
            }
        }
        return false;
    }

    // CAUTION: Assumes that isCommaFollowedByExprForm(text) == true.
    private static String extractExprWeak(String text) {
        return text.substring(1, text.length()-1);
    }

    public static int eval(String text, SymbolTable symTable, int locCounter, int normalFSetting) {
        if (isVacuous(text)) {
            return normalFSetting;
        } else if (isFormOfLParenExprRParen(text, symTable)) {
            String expr = extractExprWeak(text);
            return Expression.eval(expr, symTable, locCounter);
        }
        throw new IllegalArgumentException("text: " + text);
    }
}
