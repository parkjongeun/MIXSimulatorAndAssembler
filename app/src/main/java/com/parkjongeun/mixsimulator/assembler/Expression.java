package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class Expression {

    private static final char PLUS = '+';
    private static final char MINUS = '-';

    private static final char PRODUCT = '*';
    private static final String[] BINARY_OPERATORS = {
            "+",
            "-",
            String.valueOf(PRODUCT),
            "//", // TODO: Order
            "/", // TODO:
            ":"
    };

    public static boolean isExpression(String text, SymbolTable symbolTable) {
        // An atomic expression
        if (AtomicExpression.isAtomicExpression(text, symbolTable)) {
            System.out.print(text + " ");
            return true;
        }

        // A plus or minus sign foloowed by an atomic expression
        if (text.length() >= 1
                && isPlusOrMinusSign(text.charAt(0))
                && AtomicExpression.isAtomicExpression(text.substring(1), symbolTable)) {
            System.out.print(text + " ");
            return true;
        }

        // An expression followed by a binary operation followed by an atomic expression
        // -1+5*20/6

        if (text.length() >= 3) {
            int maxOperandIndex = -1;
            int minOperatorIndex = -1;
            for (int i = 0; i < BINARY_OPERATORS.length; ++i) {
                int operatorIndex = text.lastIndexOf(BINARY_OPERATORS[i], text.length() - 2);
                if (operatorIndex != -1) {
                    int operandIndex = operatorIndex + BINARY_OPERATORS[i].length();
                    if (operandIndex > maxOperandIndex) {
                        maxOperandIndex = operandIndex;
                        minOperatorIndex = operatorIndex;
                    } else if (operandIndex == maxOperandIndex) {
                        if (operatorIndex < minOperatorIndex) {
                            maxOperandIndex = operandIndex;
                            minOperatorIndex = operatorIndex;
                        } else if (operandIndex == minOperatorIndex) {
                            throw new IllegalStateException("Ambiguous");
                        }
                    }
                }
            }

            if (maxOperandIndex != -1) {
                if (minOperatorIndex == -1)
                    throw new IllegalStateException("Unexpected!");
                if (minOperatorIndex < 1) {
                    return false;
                }
                String exp = text.substring(0, minOperatorIndex);
                String bop = text.substring(minOperatorIndex, maxOperandIndex);
                String rhs = text.substring(maxOperandIndex);
                if (AtomicExpression.isAtomicExpression(rhs, symbolTable)
                        && isExpression(exp, symbolTable)) {
                    System.out.print(bop + " ");
                    System.out.print(rhs + " ");
                    return true;
                }
            }
        }
        return false;
    }


    private static boolean isPlusOrMinusSign(char c) {
        return c == PLUS || c == MINUS;
    }
}
