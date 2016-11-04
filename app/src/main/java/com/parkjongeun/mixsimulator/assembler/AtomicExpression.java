package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class AtomicExpression {

    private final static String ASTERISK = "*";

    public static boolean isAtomicExpressionWeak(String text) {
        // A number
        if (Number.isNumber(text))
            return true;

        // A defined symbol (denoting the numerical equivalent of that symbol)
        if (Symbol.isSymbol(text))
            return true;

        // An asterisk
        if (ASTERISK.equals(text))
            return true;

        return false;
    }

    public static boolean isAtomicExpression(String text, SymbolTable symbolTable) {
        // A number
        if (Number.isNumber(text))
            return true;

        // A defined symbol (denoting the numerical equivalent of that symbol)
        if (isDefinedSymbol(text, symbolTable))
            return true;

        // An asterisk
        if (ASTERISK.equals(text))
            return true;

        return false;
    }

    private static boolean isDefinedSymbol(String text, SymbolTable symbolTable) {
        return Symbol.isSymbol(text) && symbolTable.contains(text);
    }

    public static int eval(String text, SymbolTable symbolTable, int locationCounter) {
        if (Number.isNumber(text)) {
            return Integer.parseInt(text);
        }

        if (isDefinedSymbol(text, symbolTable)) {
            return symbolTable.get(text);
        }

        if (ASTERISK.equals(text)) {
            return locationCounter;
        }

        throw new IllegalArgumentException(text);
    }
}
