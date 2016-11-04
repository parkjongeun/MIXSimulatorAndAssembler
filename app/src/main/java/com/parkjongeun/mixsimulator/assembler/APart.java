package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class APart {

    public static boolean isAPart(String text, SymbolTable symbolTable) {
        // vacuous
        if (isVacuous(text)) {
            return true;
        }

        // Expression
        if (Expression.isExpression(text, symbolTable)) {
            return true;
        }

        // Future reference
        if (isFutureReference(text, symbolTable)) {
            return true;
        }

        // Literal constant
        if (LiteralConstant.isLiteralContant(text, symbolTable)) {
            return true;
        }
        return false;
    }

    private static boolean isVacuous(String text) {
        return "".equals(text);
    }

    private static boolean isFutureReference(String text, SymbolTable symTable) {
        if (Symbol.isSymbol(text)) {
            if (!symTable.contains(text)) {
                return true;
            }
        }
        return false;
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

    public static int eval(String text, SymbolTable symTable, int locCounter) {
        if (isVacuous(text)) {
            return 0;
        } else if (Expression.isExpression(text, symTable)) {
            return Expression.eval(text, symTable, locCounter);
        } else if (isFutureReference(text, symTable)) {

        } else if (LiteralConstant.isLiteralContant(text, symTable)) {

        }
        throw new IllegalArgumentException("text: " + text);
    }
}
