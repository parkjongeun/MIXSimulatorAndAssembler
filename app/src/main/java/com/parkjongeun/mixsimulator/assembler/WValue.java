package com.parkjongeun.mixsimulator.assembler;

import com.parkjongeun.mixsimulator.Word;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

// Word Value
public class WValue {

    public static boolean isWValue(String text, SymbolTable symbolTable) {
        int commaIndex = text.lastIndexOf(',');
        if (commaIndex != -1) {
            if (commaIndex < text.length() - 1) {
                if (isFormOfExprFPart(text.substring(commaIndex + 1), symbolTable)) {
                    if (isWValue(text.substring(0, commaIndex), symbolTable)) {
                        return true;
                    }
                }
            }
        } else {
            if (isFormOfExprFPart(text, symbolTable)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isFormOfExprFPart(String text, SymbolTable symbolTable) {
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

    private static String extractExpr(String text) {
        final int lParenIndex = text.indexOf('(');
        if (lParenIndex != -1) {
            return text.substring(0, lParenIndex);
        } else {
            return text;
        }
    }

    private static String extractFPart(String text) {
        final int lParenIndex = text.indexOf('(');
        if (lParenIndex != -1) {
            return text.substring(lParenIndex);
        } else {
            return "";
        }
    }

    private static void processExprFPartForm(String text, SymbolTable symTable, int locCounter, Word word) {
        if (!isFormOfExprFPart(text, symTable)) {
            throw new IllegalArgumentException("Syntax error: " + text);
        }

        final String expr = extractExpr(text);
        final String fpart = extractFPart(text);

        // TODO: BUG -0. ENTA.
        int valExpr = Expression.eval(expr, symTable, locCounter);
        int valFPart = FPart.eval(fpart, symTable, locCounter, 5);
        int l = valFPart / 8;
        int r = valFPart % 8;

        if (!(l <= r
                && 0 <= l && l <= 5
                && 0 <= r && r <= 5)) {
            throw new IllegalArgumentException("L: " + l + " R: " + r);
        }

        Word wordExpr = new Word();
        wordExpr.setQuantity(valExpr < 0 ? Word.MINUS : Word.PLUS, valExpr);

        for (int dst = r, src = Word.COUNT_OF_BYTES_IN_WORD; dst >= l && dst > 0; --dst, --src) {
            word.setField(dst, wordExpr.getField(src));
        }

        if (l == 0) {
            word.setSign(wordExpr.getSign());
        }
    }

    public static Word assemble(String text, SymbolTable symTable, int locCounter) {
        if (!isWValue(text,symTable)) {
            throw new IllegalArgumentException("Syntax error: " + text);
        }
        String[] stmts = text.split(",");

        Word word = new Word();
        for (int i = 0; i < stmts.length; ++i) {
            processExprFPartForm(stmts[i], symTable, locCounter, word);
        }
        return word;
    }


}
