package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class Expression {

    private static final char PLUS = '+';
    private static final char MINUS = '-';

    private static final String ADD = "+";
    private static final String SUB = "-";
    private static final String PRODUCT = "*";
    private static final String DIVIDE = "/";
    private static final String FRACTION = "//";
    private static final String ENCODEFIELDSPEC = ":";

    private static final String[] BINARY_OPERATORS = {
            ADD,
            SUB,
            PRODUCT,
            FRACTION, // TODO: Order
            DIVIDE, // TODO:
            ENCODEFIELDSPEC
    };

    public static boolean isExpression(String text, SymbolTable symbolTable) {
        // An atomic expression
        if (AtomicExpression.isAtomicExpression(text, symbolTable)) {
            System.out.print(text + " ");
            return true;
        }

        // A plus or minus sign followed by an atomic expression
        if (isPlusOrMinusSignFollowedByAtomicExpression(text, symbolTable)) {
            System.out.print(text + " ");
            return true;
        }

        // An expression followed by a binary operation followed by an atomic expression
        final String[] outTokens = new String[3];
        if (TokenizeIntoExprBinaryOpAtomicExpr(text, outTokens)) {
            if (Expression.isExpression(outTokens[0], symbolTable)
                    && AtomicExpression.isAtomicExpression(outTokens[2], symbolTable)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPlusOrMinusSignFollowedByAtomicExpression(String text, SymbolTable symTable) {
        if (text.length() >= 1
                && isPlusOrMinusSign(text.charAt(0))
                && AtomicExpression.isAtomicExpression(text.substring(1), symTable)) {
            return true;
        }
        return false;
    }

    public static int eval(String text, SymbolTable symTable, int locCounter) {
        if (AtomicExpression.isAtomicExpression(text, symTable)) {
            return AtomicExpression.eval(text, symTable, locCounter);
        } else if (isPlusOrMinusSignFollowedByAtomicExpression(text, symTable)) {
            return (text.charAt(0) == '+' ? 1 : -1) * AtomicExpression.eval(text.substring(1), symTable, locCounter);
        }

        final String[] outTokens3 = new String[3];
        if (TokenizeIntoExprBinaryOpAtomicExpr(text, outTokens3)) {
            int valExpr = eval(outTokens3[0], symTable, locCounter);
            int valAtomicExpr = AtomicExpression.eval(outTokens3[2], symTable, locCounter);
            String binaryOp = outTokens3[1];
            return applyBinaryOp(binaryOp, valExpr, valAtomicExpr);
        }
        throw new IllegalArgumentException();
    }

    private static int applyBinaryOp(final String op, final int a, final int b) {
        if (ADD.equals(op)) {
            return a + b;
        } else if (SUB.equals(op)) {
            return a - b;
        } else if (PRODUCT.equals(op)) {
            return a * b;
        } else if (DIVIDE.equals(op)) {
            return a / b;
        } else if (FRACTION.equals(op)) {
            // TODO:
            throw new UnsupportedOperationException();
        } else if (ENCODEFIELDSPEC.equals(op)) {
            return 8 * a + b;
        }
        throw new IllegalArgumentException();
    }


    // -1+5*20/6  ->  (-1+5*20) (/) (6)
    // -1+5*20**  ->  (-1+5*20) (*) (*)
    // ***  ->  (*) (*) (*)
    // *  -> () () *
    // **  -> () * *
    private static boolean TokenizeIntoExprBinaryOpAtomicExpr(final String text, final String[] outTokens) {
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
                final String exp = text.substring(0, minOperatorIndex);
                final String bop = text.substring(minOperatorIndex, maxOperandIndex);
                final String rhs = text.substring(maxOperandIndex);

                outTokens[0] = exp;
                outTokens[1] = bop;
                outTokens[2] = rhs;
                return true;
            }
        }
        return false;
    }


    private static boolean isPlusOrMinusSign(char c) {
        return c == PLUS || c == MINUS;
    }
}
