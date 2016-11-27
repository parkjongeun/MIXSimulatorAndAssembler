package com.parkjongeun.mixsimulator.assembler;


/**
 * Created by Parkjongeun on 02/11/2016.
 */

public class LiteralConstant {


    public static boolean isLiteralContant(String text, SymbolTable symbolTable) {
        String wValue = extractWValueWeak(text);
        if (!"".equals(wValue)) {
            if (WValue.isWValue(wValue, symbolTable)) {
                return true;
            }
        }
        return false;
    }


    // =1-L=
    // =3=
    public static Word assemble(String text, SymbolTable symTable, int locCounter) {
        String wValue = extractWValueWeak(text);
        if (!"".equals(wValue)) {
            return WValue.assemble(wValue, symTable, locCounter);
        }
        throw new IllegalArgumentException();
    }

    static String extractWValueWeak(String str) {
        if (!(str.length() >= 3)) {
            return "";
        }
        final char first = str.charAt(0);
        final char last = str.charAt(str.length()-1);
        if (first == '=' && last == '=') {
            return str.substring(1, str.length()-1);
        } else {
            return "";
        }
    }

}
