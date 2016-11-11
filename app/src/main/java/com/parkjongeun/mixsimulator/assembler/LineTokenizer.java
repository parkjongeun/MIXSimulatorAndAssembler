package com.parkjongeun.mixsimulator.assembler;



/**
 * Created by Parkjongeun on 11/11/2016.
 */

public class LineTokenizer {



    static final String L1 = "L EQU 500\n";
    static final String L2 = "2H OUT TITLE(PRINTER)\n";
    static final String L3 = " ALF FIRST\n";
    static final String L4 = " ALF \" FIVE\"\n";


    public static Assembler.Line tokenizeIntoLOC_OP_ADDRESS(final String str) {
        if (str == null) {
            throw new IllegalArgumentException("str == null");
        }
        if (str.length() == 0) {
            throw new IllegalArgumentException("str.length == 0");
        }
        if (str.startsWith("*")) {
            throw new IllegalArgumentException("It's comment: " + str);
        }

        int firstBlankSpaceIndex = indexOfFirstBlankSpace(str);

        if (firstBlankSpaceIndex == -1) {
            throw new IllegalArgumentException("Invalid LOC field: " + str);
        }

        String LOC = str.substring(0, firstBlankSpaceIndex);

        int OPIdxB = findNonBlackSpace(str, firstBlankSpaceIndex);
        if (OPIdxB == -1) {
            throw new IllegalArgumentException("OP field expected: " + str);
        }
        int OPIdxE = findBlackSpace(str, OPIdxB);
        if (OPIdxE == -1) {
            throw new IllegalArgumentException("Blank space expected: " + str);
        }
        String OP = str.substring(OPIdxB,OPIdxE);
        String ADDRESS;

        if (OP.equals("EQU")
                || OP.equals("ORIG")
                || OP.equals("CON")
                || OP.equals("END")) {
            int ADDRESSIdxB = findNonBlackSpace(str, OPIdxE);
            if (ADDRESSIdxB == -1) {
                throw new IllegalArgumentException("W-value expected: " + str);
            }
            int ADDRESSIdxE = findBlackSpace(str, ADDRESSIdxB);
            if (ADDRESSIdxE == -1) {
                throw new IllegalArgumentException("Blank space expected: " + str);
            }
            ADDRESS = str.substring(ADDRESSIdxB, ADDRESSIdxE);
        } else if (OP.equals("ALF")) {
            if (OPIdxE == str.length()-1) {
                ADDRESS = "     ";
            } else {
                char[] chars = new char[5];
                if (str.charAt(OPIdxE + 1) == '"') {
                    if (OPIdxE + 2 == str.length()) {
                        throw new IllegalArgumentException("Closing \" expected: " + str);
                    }
                    int closingQuote = str.indexOf('"', OPIdxE + 2);
                    if (closingQuote == -1) {
                        throw new IllegalArgumentException("Closing \" expected: " + str);
                    }
                    String char1 = str.substring(OPIdxE + 2, closingQuote);
                    if (char1.length() > 5) {
                        throw new IllegalArgumentException("Exceeds 5 Characters: " + str);
                    }
                    ADDRESS = char1;
                } else {
                    // TODO: BUG.
                    for (int i = OPIdxE + 1, j = 0; i < str.length() - 1 && i < OPIdxE + 6; ++i, ++j) {
                        chars[j] = str.charAt(i);
                    }
                    if (!isBlankSpace(str.charAt(str.length() - 1))) {
                        throw new IllegalArgumentException("Blank space expected: " + str);
                    }
                    ADDRESS = String.valueOf(chars);
                }
            }
        } else if (isSymbolicMIXOperator(OP)) {
            if (OPIdxE == str.length()-1) {
                ADDRESS = "";
            } else {
                int ADDRESSIdxB = OPIdxE + 1;
                int ADDRESSIdxE = findBlackSpace(str, OPIdxE + 1);
                if (ADDRESSIdxE == -1) {
                    throw new IllegalArgumentException("Blank space expected: " + str);
                }
                ADDRESS = str.substring(ADDRESSIdxB, ADDRESSIdxE);
            }
        } else {
            throw new IllegalArgumentException("Invalid OP field: " + OP);
        }
        Assembler.Line L = new Assembler.Line();
        L.colLOC = LOC;
        L.colOP = OP;
        L.colADDRESS = ADDRESS;
        return L;
    }

    // TODO: Could be improved!
    private static boolean isSymbolicMIXOperator(String str) {
        for (OpCode opCode : OpCode.values()) {
            if (opCode.name().equals(str))
                return true;
        }
        return false;
    }

    private static int findNonBlackSpace(CharSequence charSeq, int from) {
        if (from < 0) {
            throw new IllegalArgumentException("from < 0");
        }
        if (!(from < charSeq.length())) {
            throw new IllegalArgumentException("from >= charSeq.length()");
        }

        for (int i = from; i < charSeq.length(); ++i) {
            if (!isBlankSpace(charSeq.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int findBlackSpace(CharSequence charSeq, int from) {
        if (from < 0) {
            throw new IllegalArgumentException("from < 0");
        }
        if (!(from < charSeq.length())) {
            throw new IllegalArgumentException("from >= charSeq.length()");
        }

        for (int i = from; i < charSeq.length(); ++i) {
            if (isBlankSpace(charSeq.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int indexOfFirstBlankSpace(CharSequence charSeq) {
        for (int i = 0; i < charSeq.length(); ++i) {
            if (isBlankSpace(charSeq.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int indexOfFirstBlankSpace(char[] str) {
        return indexOfFirstBlankSpace(new String(str));
    }

    private static boolean isBlankSpace(char c) {
        return Character.isWhitespace(c);
    }
}
