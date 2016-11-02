package com.parkjongeun.mixsimulator.assembler;

/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class APart {

    public static boolean isAPart(String text, SymbolTable symbolTable) {
        // vacuous
        if ("".equals(text)) {
            return true;
        }

        // Expression
        if (Expression.isExpression(text, symbolTable)) {
            return true;
        }

        // Future reference
        if (Symbol.isSymbol(text)) {
            if (!symbolTable.contains(text)) {
                return true;
            }
        }

        // Literal constant
        if (LiteralConstant.isLiteralContant(text, symbolTable)) {
            return true;
        }
        return false;
    }

}
