package com.parkjongeun.mixsimulator.legacy;

import com.parkjongeun.mixsimulator.Memory;
import com.parkjongeun.mixsimulator.OpCode;
import com.parkjongeun.mixsimulator.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Parkjongeun on 02/10/2016.
 */

// MIX Assembly Language Assembly Program
public class Assembler {


    String mPgm =
            "* EXAMPLE PROGRAM ... TABLE OF PRIMES\n" +
                    "*\n" +
                    "L EQU 500 The number of primes to find\n" +
                    "PRINTER EQU 18 Unit number of the line printer\n" +
                    "PRIME EQU -1 Memory area for table of primes\n" +
                    "BUF0 EQU 2000 Memory area for BUFFER[0]\n" +
                    "BUF1 EQU BUF0+25 Memory area for BUFFER[1]\n" +
                    " ORIG 3000\n" +
                    "START IOC 0(PRINTER) Skip to new page.\n" +
                    " LD1 =1-L= P1. Start table. J<-1.\n" +
                    " LD2 =3= N<-3.\n" +
                    "2H INC1 1 P2. N is prime. j<-J+1.\n" +
                    " ST2 PRIME+L,1 PRIME[J]<-N.\n" +
                    " J1Z 2F\n" +
                    "4H INC2 2\n" +
                    " ENT3 2\n" +
                    "6H ENTA 0\n" +
                    " ENTX 0,2\n" +
                    " DIV PRIME,3\n" +
                    " JXZ 4B\n" +
                    " CMPA PRIME,3\n" +
                    " INC3 1\n" +
                    " JG 6B\n" +
                    " JMP 2B\n" +
                    "2H OUT TITLE(PRINTER)\n" +
                    " ENT4 BUF1+10\n" +
                    " ENT5 -50\n" +
                    "2H INC5 L+1\n" +
                    "4H LDA PRIME,5\n" +
                    " CHAR  Convert PRIME[M] to decimal.\n" +
                    " STX 0,4(1:4)\n" +
                    " DEC4 1\n" +
                    " DEC5 50\n" +
                    " J5P 4B\n" +
                    " OUT 0,4(PRINTER)\n" +
                    " LD4 24,4\n" +
                    " J5N 2B\n" +
                    " HLT\n" +
                    "* INITIAL CONTENTS OF TABLES AND BUFFERS\n" +
                    " ORIG PRIME+1\n" +
                    " CON 2\n" +
                    " ORIG BUF0-5\n" +
                    "TITLE ALF \"FIRST\"\n" +
                    " ALF \" FIVE\"\n" +
                    " ALF \" HUND\"\n" +
                    " ALF \"RED P\"\n" +
                    " ALF \"RIMES\"\n" +
                    " ORIG BUF0+24\n" +
                    " CON BUF1+10\n" +
                    " ORIG BUF1+24\n" +
                    " CON BUF0+10\n" +
                    " END START End of routine.\n";


    private final static String ptSymbol = "[A-Z0-9]{1,10}";
    private final static String ptNumber = "\\d{1,10}";
    private final static String ptAtomicExpr = ptNumber + "|" + ptSymbol + "|\\*";
    private final static String ptExprRaw = String.format("(?:(?:%1$s)|(?:[+-](?:%1$s)))" +
            "(?:(?:\\+|-|\\*|/|//|:)(?:%1$s))*", ptAtomicExpr);
    private final static String ptExprAssoc = String.format("((?:%1$s)|(?:[+-](?:%1$s)))" +
            "(?:(\\+|-|\\*|/|//|:)(%1$s))", ptAtomicExpr);

    private final static String regexBinaryOperator = "(\\+|-|\\*|/|//|:)";
    private final static String regexBinaryOperatorRaw = "\\+|-|\\*|/|//|:";
    private final static String regexExprBinaryOpAtomicExpr =
            String.format("(%1$s)(%2$s)(%3$s)", ptExprRaw, regexBinaryOperatorRaw, ptAtomicExpr);

    private final static String regexFPartRaw = "\\(" + ptExprRaw + "\\)";
    private final static String regexExprFPart = String.format("(%1$s)(%2$s)?", ptExprRaw, regexFPartRaw);
    private final static String regexExprFPartRaw = String.format("(?:%1$s)(?:%2$s)", ptExprRaw, regexFPartRaw);
    private final static String regexWValueRaw = String.format("(?:%1$s)(?:,(?:%1$s))*", regexExprFPartRaw);
    private final static String regexWValueCommaWvalue = String.format("(%1$s),(%2$s)(%3$s)", regexWValueRaw, ptExprRaw, regexFPartRaw);

    private final static String ptFPart = "(?:\\((?<expr>"+ ptExprRaw + ")\\))?";
    private final static String ptFPartRaw = "(?:\\((?:"+ ptExprRaw + ")\\))?";
    private final static String ptIPart = "(,(?<expr>" + ptExprRaw + "))?";
    private final static String ptIPartRaw = "(?:,(?:" + ptExprRaw + "))?";
    private final static String ptWValueRaw = String.format("%1$s%2$s(?:,%1$s%2$s)*", ptExprRaw, ptFPartRaw);
    private final static String ptExprFpart = String.format("(?<expr>%1$s)(?<fpart>%2$s)", ptExprRaw, ptFPartRaw);
    private final static String ptLiteralConst = "=(?<wvalue>" + ptWValueRaw + ")=";
    private final static String ptLiteralConstRaw = "=(?:" + ptWValueRaw + ")=";
    private final static String ptAPartRaw = String.format("(?:(?:%1$s)|(?:%2$s))?", ptExprRaw, ptLiteralConstRaw);
    private final static String ptAPart = String.format("(?:(%1$s)|(%2$s))?", ptExprRaw, ptLiteralConstRaw);

    private final static String ptAIF = String.format("(?<apart>%1$s)(?<ipart>%2$s)(?<fpart>%3$s)", ptAPartRaw, ptIPartRaw, ptFPartRaw);


    private final static Pattern pttExprFPart = Pattern.compile(ptExprFpart);
    private final static Pattern pttFpart = Pattern.compile(ptFPart);
    private final static Pattern pptAIF = Pattern.compile(ptAIF);
    private final static Pattern pttIPart = Pattern.compile(ptIPart);
    private final static Pattern pttAPart = Pattern.compile(ptAPart);
    private final static Pattern pttSymbol = Pattern.compile(ptSymbol);
    private final static Pattern pttLocalSymbol = Pattern.compile("(\\d)([HFB])");
    private final static Pattern pttLiteralConst = Pattern.compile(ptLiteralConst);
    private final static Pattern pttWValueCommaWValue = Pattern.compile(regexWValueCommaWvalue);


    // 주소 개수기
    private int locCounter;

    // 심볼표
    private final Map<String, String> symTable = new HashMap<>();

    // 퓨쳐 참조
    private final Map<Integer, String> futureRef = new HashMap<>();
    // 리터럴 상수
    private final Map<String, Word> literalConstMap = new HashMap<>();
    // 리터럴 상수명 할당기
    private int literalConstAllocator = 0;



    private void processFutureRef(final Memory memory) {
        Set<Integer> addresses = futureRef.keySet();

        for (int address : addresses) {
            // Only relevant to the A-part.
            final Word word = memory.get(address);

            final String symbol = futureRef.get(address);

            final String value = symTable.get(symbol);
            if (value == null) {
                throw new IllegalStateException("Undefined symbol: " + symbol);
            }

            final int valueAsInt = Integer.parseInt(value);

            // TODO: -0.
            final int sign = valueAsInt < 0 ? Word.MINUS : Word.PLUS;

            word.setAddress(sign, Math.abs(valueAsInt));
        }

    }

    private void processLiteralConstant(final Memory memory) {
        Set<String> keys = literalConstMap.keySet();

        for (String name : keys) {

            final Word word = literalConstMap.get(name);

            memory.write(locCounter, word);

            symTable.put(name, String.valueOf(locCounter));
            locCounter++;
        }
    }

    private void processUndefinedSymbols(final Memory memory) {
        Set<Integer> addresses = futureRef.keySet();

        for (int address : addresses) {
            final String symbol = futureRef.get(address);

            // If the symbol never appears in LOC, a new line is effectively inserted before the END line,
            // having OP = "CON" and ADDRESS = "0" and the name of the symbol in LOC. (TAOCP1 p. 156)
            if (!symTable.containsKey(symbol)) {
                memory.write(locCounter, new Word());
                symTable.put(symbol, String.valueOf(locCounter));
                locCounter++;
            }
        }
    }

    // Not necessary.
    // We can assemble in one-pass.
    void listing(List<Line> lines) {
        //int lc = 0;
        locCounter = 0;

        for (int i = 0; i < lines.size(); ++i) {
            Line line = lines.get(i);

            if (!line.colLOC.isEmpty()) {
                symTable.put(line.colLOC, String.valueOf(locCounter));
            }

            if (!isMIXALDirective(line.colOP)) {
                line.location = locCounter;
                ++locCounter;
            } else if ("ORIG".equals(line.colOP)) {
                if (isWValue(line.colADDRESS)) {
                    try {
                        locCounter = Integer.parseInt(evalWValue(line.colADDRESS));
                    } catch (NumberFormatException e) {
                        throw new IllegalStateException();
                    }
                } else {
                    throw new IllegalArgumentException("The ADDRESS should be a W-value.");
                }
            }
        }
    }

    Memory assemble() {
        final List<Line> lines = parse(mPgm);

        resolveLocalSymbols(lines);

        final Memory memory = new Memory();
        locCounter = 0;

        for (int i = 0; i < lines.size(); ++i) {
            final Line line = lines.get(i);

            if ("EQU".equals(line.colOP)) {
                processEQUDirective(line);
            } else if ("ORIG".equals(line.colOP)) {
                processORIGDirective(line);
            } else if ("CON".equals(line.colOP)) {
                Word word = processCONDirective(line);
                memory.write(locCounter, word);
                locCounter++;
            } else if ("ALF".equals(line.colOP)) {
                Word word = processALFDirective(line);
                memory.write(locCounter, word);
                locCounter++;
            } else if ("END".equals(line.colOP)) {
                processLiteralConstant(memory);
                processUndefinedSymbols(memory);
                if (!line.colLOC.isEmpty()) {
                    symTable.put(line.colLOC, String.valueOf(locCounter));
                }
                processFutureRef(memory);

                final Word word = evalWValueW(line.colADDRESS);
                final int startFrom = word.getQuantity(4, 5);

            } else {
                Word word = processMIXOperator(line);
                memory.write(locCounter, word);
                locCounter++;
            }
        }

        return memory;
    }

    private Word processCONDirective(Line line) {
        if (!"CON".equals(line.colOP)) {
            throw new IllegalArgumentException("OP value must be 'CON': " + line.colOP);
        }

        final String loc = line.colLOC;
        final String address = line.colADDRESS; // address == 'w-value'

        final Word word = evalWValueW(address);

        if (!loc.isEmpty()) {
            symTable.put(loc, String.valueOf(locCounter));
        }

        return word;
    }

    private Word processALFDirective(Line line) {
        if (!"ALF".equals(line.colOP)) {
            throw new IllegalArgumentException("OP value must be 'ALF': " + line.colOP);
        }

        final String loc = line.colLOC;
        final String address = line.colADDRESS; // address == 'w-value'

        final Word word = new Word();

        if (address.length() > Word.COUNT_OF_BYTES_IN_WORD) {
            throw new IllegalArgumentException("Too many characters: " + address);
        }

        for (int i = 0; i < address.length(); ++i) {
            final int code = charToCode(address.charAt(i));
            word.setField(1 + i, code);
        }

        if (!loc.isEmpty()) {
            symTable.put(loc, String.valueOf(locCounter));
        }

        return word;
    }

    static boolean isMIXALDirective(String text) {
        return text.equals("EQU")
                || text.equals("ORIG")
                || text.equals("CON")
                || text.equals("ALF")
                || text.equals("END");
    }

    void resolveLocalSymbols(List<Line> lines) {
        // 0H ~ 9H
        // 0H1, 0H2, 0H3, ...
        // 1H1, 1H2, 1H3, ...
        // ...
        // 9H1, 9H2, 9H3, ...
        // If subscription[0] == 3 then 0H1, 0H2, 0H3 had been defined.
        final int[] subscription = new int[10];

        final Pattern pttLocalSymbolH = Pattern.compile("(\\d)(H)");
        final Pattern pttLocalSymbolB = Pattern.compile("(\\d)([BF])");


        for (int i = 0; i < lines.size(); ++i) {
            final Line line = lines.get(i);
            Matcher mat = pttLocalSymbolH.matcher(line.colLOC);
            if (mat.matches()) {
                int d = Integer.parseInt(mat.group(1));
                ++subscription[d];
                line.colLOC = mat.group() + subscription[d];
            }

            Matcher mat2 = pttLocalSymbolB.matcher(line.colADDRESS);
            while (mat2.find()) {
                int d = Integer.parseInt(mat2.group(1));
                String fOrB = mat2.group(2);
                if (fOrB.equals("F")) {
                    line.colADDRESS = line.colADDRESS.replace(mat2.group(), d + "H" + (subscription[d] + 1));
                } else if (fOrB.equals("B")) {
                    line.colADDRESS = line.colADDRESS.replace(mat2.group(), d + "H" + subscription[d]);
                }
            }
        }
    }

    void processORIGDirective(Line l) {
        String symbol = l.colLOC;
        if (!symbol.isEmpty()) {
            symTable.put(symbol, String.valueOf(locCounter));
        }
        if (isWValue(l.colADDRESS)) {
            String addr = evalWValue(l.colADDRESS);
            locCounter = Integer.parseInt(addr);
        } else {
            error("WValue expected.");
            return;
        }
    }

    void processEQUDirective(Line line) {
        String symbol = line.colLOC;
        if (symbol.isEmpty()) {
            error("Defined symbol missing.");
            return;
        }
        if (isWValue(line.colADDRESS)) {
            String addr = evalWValue(line.colADDRESS);
            // TODO:
            symTable.put(symbol, addr);
        } else {
            error("WValue expected.");
            return;
        }
    }

    private final int A1_FIELD = 1;
    private final int A2_FIELD = 2;
    private final int I_FIELD = 3;
    private final int F_FIELD = 4;
    private final int C_FIELD = 5;

    Word processMIXOperator(Line line) {
        String symbol = line.colLOC;
        if (!symbol.isEmpty()) {
            String loc = String.valueOf(locCounter);
            symTable.put(symbol, loc);
        }

        // Parse AIF
        Matcher mtt = pptAIF.matcher(line.colADDRESS);
        if (mtt.matches()) {
            String aPart = mtt.group(1);
            String iPart = mtt.group(2);
            String fPart = mtt.group(3);
            String opCodeSym = line.colOP;

            OpCode opCode = OpCode.valueOf(opCodeSym);
            Word mixWord = new Word();
            mixWord.setC(opCode.code);
            if (!fPart.isEmpty()) {
                // TODO: 좀 더 정교하게 하기.
                mixWord.setF(evalFPart(fPart));
            } else {
                mixWord.setF(opCode.fieldSpec); // Normal Field Spec.
            }

            if (!iPart.isEmpty()) {
                mixWord.setI(evalIPart(iPart));
            } else {
                mixWord.setI(0); // vacuous (denoting the value zero)
            }

            if (!aPart.isEmpty()) {
                int address = evalAPart(locCounter, aPart);
                // TODO: Correct the sign. In case of -0.
                int sign = address < 0 ? Word.MINUS : Word.PLUS;
                mixWord.setAddress(sign, Math.abs(address));
            } else {
                mixWord.setAddress(Word.PLUS, 0); // vacuous (denoting the value zero)
            }
            return mixWord;
        } else {
            throw new IllegalArgumentException("Invalid format.");
        }
    }

    boolean isWValue(String token) {
        Pattern ptt = Pattern.compile(ptWValueRaw);
        return ptt.matcher(token).matches();
    }

    public String evalWValue(String token) {
        String[] exprFPart = token.split(",");

        Matcher mat = pttExprFPart.matcher(exprFPart[0]);
        mat.matches();
        String expr = mat.group(1);
        String fPart = mat.group(2);

        return String.valueOf(evalExpr(expr));
    }

    public Word evalWValueW(String text) {
        // -0
        // Expr F-part
        //

        Pattern pttExprFPart  = Pattern.compile(regexExprFPart);

        Matcher mat = pttExprFPart.matcher(text);
        if (mat.matches()) {
            int exprVal = evalExpr(mat.group(1));
            int fpartVal;
            final int sign = exprVal < 0 ? Word.MINUS : Word.PLUS; // TODO:
            Word word = new Word();
            word.setQuantity(sign, exprVal);
            return word;
        } else {
            Matcher mat2 = pttWValueCommaWValue.matcher(text);
            if (mat2.matches()) {
                Word word = evalWValueW(mat2.group(1));
                int exprVal = evalExpr(mat2.group(2));
                int fpartVal;
                final int sign = exprVal < 0 ? Word.MINUS : Word.PLUS; // TODO:
                word.setQuantity(sign, exprVal);
                return word;
            }
        }
        throw new IllegalArgumentException("Illegal W-value: " + text);
        /*if (mat.matches()) {
            final Word word = new Word();

            while (true) {
                final String expr = mat.group(2);
                final String fPart = mat.group(3); // TODO:

                final int val = evalExpr(expr);
                final int sign = val < 0 ? Word.MINUS : Word.PLUS; // TODO:
                word.setQuantity(sign, val);

                if (mat.group(4) == null) {
                    break;
                }
                mat = pttWValue.matcher(mat.group(4));
                mat.matches();
            }

            return word;
        }*/
    }

    public int evalFPart(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Argument 'text' is null or empty.");
        }
        Matcher mat = pttFpart.matcher(text);
        if (!mat.matches()) {
            throw new IllegalArgumentException("Illegal F-Part format.");
        }
        String expr = mat.group(1);
        return evalExpr(expr);
    }

    private int evalIPart(String text) {
        if (text.isEmpty()) {
            return 0; // vacuous (denoting the value zero)
        } else {
            Matcher mtt = pttIPart.matcher(text);
            if (!mtt.matches()) {
                throw new IllegalArgumentException("Invalid I-Part.");
            }
            String expr = mtt.group(2);
            if (expr != null) {
                return evalExpr(expr); // (denoting the value of expression.)
            } else {
                throw new IllegalStateException("Assertion failed.");
            }
        }
    }

    private int evalAPart(int location, String text) {
        if (text.isEmpty()) {
            return 0; // vacuous (denoting the value zero)
        }
        Matcher mtt = pttAPart.matcher(text);
        if (!mtt.matches()) {
            throw new IllegalArgumentException("Invalid A-Part format.");
        }

        String exprOrFutureRef = mtt.group(1);
        String literalConst = mtt.group(2);

        if (literalConst != null) { // a literal constant
            Matcher mat = pttLiteralConst.matcher(text);
            mat.matches();
            int wValue = Integer.parseInt(evalWValue(mat.group(1)));
            Word word = new Word();
            word.setQuantity(wValue < 0 ? Word.MINUS : Word.PLUS, wValue);
            String literalSymbol = "literal" + (++literalConstAllocator);
            literalConstMap.put(literalSymbol, word);
            futureRef.put(location, literalSymbol);
        } else {
            if (isFutureReference(text)) { // a future reference
                // dF, TITLE, etc.
                futureRef.put(location, text);
            } else { // an expression
                return evalExpr(exprOrFutureRef);
            }
        }
        return  0;
    }

    boolean isFutureReference(String text) {
        Matcher mat = pttSymbol.matcher(text);
        if (mat.matches()) {
            String symbol = mat.group();
            if (!isNumber(symbol)) {
                if (isLocalSymbol(symbol)) {
                    if ('F' == symbol.charAt(1)) {
                        return true;
                    }
                } else if (!symTable.containsKey(symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isLocalSymbol(String text) {
        return pttLocalSymbol.matcher(text).matches();
    }


    // Recursive Descent Parsing.
    private int evalExpr(final String expr) {
        if (isAtomicExpression(expr)) {
            return evalAtom(expr);
        } else if (expr.charAt(0) == '+' && isAtomicExpression(expr.substring(1))) {
            return evalAtom(expr.substring(1));
        } else if (expr.charAt(0) == '-' && isAtomicExpression(expr.substring(1))) {
            return -evalAtom(expr.substring(1));
        } else {
            // ex) E. + A.E. - A.E. / A.E.
            final Pattern patternExprBinaryOpAtomicExpr = Pattern.compile(regexExprBinaryOpAtomicExpr);
            Matcher mat = patternExprBinaryOpAtomicExpr.matcher(expr);
            if (mat.matches()) {
                final String expr_ = mat.group(1);
                final String operator = mat.group(2);
                final String aexpr = mat.group(3);
                final int a = evalExpr(expr_);
                final int b = evalAtom(aexpr);
                final int val = applyBinaryOp(operator, a, b);
                return val;
            }
        }
        throw new IllegalArgumentException("Illegal Expression: " + expr);
    }

    static boolean isNumber(String atom) {
        if (1 <= atom.length() && 10 >= atom.length()) {
            for (int i = 0; i < atom.length(); ++i) {
                if (!Character.isDigit(atom.charAt(i)))
                    return false;
            }
            return true;
        } else
            return false;
    }

    private boolean isDefinedSymbol(String text) {
        return symTable.containsKey(text);
    }

    private boolean isAtomicExpression(String text) {
        // Number | Defined Symbol | *
        return isNumber(text) | isDefinedSymbol(text) | "*".equals(text);
    }

    private int evalAtom(String atom) {
        if ("*".equals(atom)) {
            return locCounter;
        } else if (isNumber(atom)){
            return Integer.parseInt(atom);
        } else if (symTable.containsKey(atom)) {
            return Integer.parseInt(symTable.get(atom));
        }
        error("This is not atomic expression: " + atom);
        throw new IllegalArgumentException("This is not atomic expression: " + atom);
    }

    int applyBinaryOp(String operatorT, int lhs, int rhs) {
        int lhsT = lhs;
        int rhsT = rhs;

        if ("-".equals(operatorT))
            return lhsT - rhsT;
        else if ("+".equals(operatorT))
            return lhsT + rhsT;
        else if ("*".equals(operatorT))
            return lhsT * rhsT;
        else if ("/".equals(operatorT))
            return lhsT / rhsT;
        else if ("//".equals(operatorT)) {
            if (lhsT < rhsT) {
                // TODO:
                long bytePow5 = (long) Math.pow(64, 5);
                if (bytePow5 > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException(String.valueOf(bytePow5));
                }
                if (lhsT * bytePow5 > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException(lhsT + "*" + bytePow5);
                }
                return (lhsT * (int) bytePow5) / rhsT;
            } else
                throw new IllegalArgumentException(lhsT + "//" + rhsT);
        } else if (":".equals(operatorT))
            return 8 * lhsT + rhsT; // 8L + R
        else
            throw new IllegalStateException(operatorT);
    }

    void exit() {

    }

    void error() {

    }

    void error(String msg) {

    }


    List<Line> parse(String pgm) {
        final Scanner scanner = new Scanner(pgm);
        final List<Line> lines = new ArrayList<>();

        while(scanner.hasNextLine()) {
            String ln = scanner.nextLine();

            if (ln.startsWith("*"))
                continue;

            Line l = lexLine(ln);
            if (l == null)
                throw new IllegalStateException();
            lines.add(l);
        }
        return lines;
    }

    // LINE -> LOC WS OP WS ADDRESS WS REMARKS
    // LOC -> ' ' | SYMBOL
    // WS -> WS ' ' | WS '\t' | e



    Line lexLine(String line) {
        String[] tok = new String[4];
        String[] tok_ = line.split("[ \\t]", 4);
        for (int i = 0; i < tok.length; ++i)
            tok[i] = "";
        for (int i = 0; i < tok_.length; ++i) {
            if (tok_[i] != null) {
                tok[i] = tok_[i];
            }
        }

        if (tok[0].startsWith("*")) {
            // error()
            return null;
        }

        if ("ALF".equals(tok[1])) {
            Pattern pattern = Pattern.compile("\"(.{0,5})\"");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                tok[2] = matcher.group(1);
                tok[3] = "";
            } else {
                // error()
                return null;
            }
        }

        Line l = new Line();
        l.colLOC = tok[0];
        l.colOP = tok[1];
        l.colADDRESS = tok[2];
        l.colRemarks = tok[3];
        return l;
    }

    public static class Token {


    }

    void assertTrue(boolean expression) {
        if (!expression) {
            throw new IllegalStateException("Assertion failed.");
        }
    }


    // Line no. LOC OP ADDRESS Remarks

    public static class Line {
        String colLOC;
        String colOP;
        String colADDRESS;
        String colRemarks;

        int location = -1;

        int lineNo;

        public Line() {
            colLOC = null;
            colOP = null;
            colADDRESS = null;
            colRemarks = null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (location != -1) {
                sb.append(location);
            } else {
                sb.append("-");
            }
            sb.append(": ");

            sb.append(colLOC);
            sb.append(", ");
            sb.append(colOP);
            sb.append(", ");
            sb.append(colADDRESS);
            sb.append(", ");
            sb.append(colRemarks);
            return sb.toString();
        }
    }



    // 1. A symbol is a string of one to ten letters and/or digits,
    // containing at least one letter. Examples: PRIME, TEMP, 20BY20.
    // The special symbols dH, dF, and  dB, where d is a single digit,
    // will for the purpose of this definition be replaced by other
    // unique symbols according to the "local symbol" convention
    // described earlier.

    // 2. A number is a string of one to ten digits.
    // Example: 00052, 1234567890, 000123000, 52.

    // 3. "defined symbol" or "future reference"

    // 4. "atomic expression"
    // a number | a defined symbol | an *

    // 5. "expression"
    // an atomic expression
    // | (+ | -) an atomic expression
    // | an expression (+ | - | * | / | // | :) an atomic expression

    // 6. "A-part"
    // vacuous
    // | expression
    // | future reference
    // | literal constant

    // 7. "I-part"
    // vacuous
    // | , expression

    // 8. "F-part"
    // vacuous
    // | ( expression )

    // 9. "W-value"
    // expression F-part
    // w-value , expression F-part

    // 10. *(location counter)(nonnegative number that can fit in two bytes)

    // 11. After processing the LOC field as described in rule 10,
    // the assembly process depends on the value of the OP field.
    // There are six possibilities for OP:
    // a) A symbolic MIX operator
    // ADDRESS: A-part followed by I-part followed by F-part.
    // LDA C; STA WORD; LDA F; STA WORD(4:4); LDA I; STA WORD(3:3) LDA A;
    // STA WORD(0:2)
    // *++: WORD
    // b) "EQU"
    // ADDRESS: W-value
    // "defined symbol" == W-value
    // BYTESIZE EQU 1(4:4)
    // BYTESIZE == 10(byte size)
    // c) "ORIG"
    // ADDRESS: W-value
    // * => W-value
    // TABLE ORIG *+100
    // TABLE == * before it has changed.
    // d) "CON"
    // ADDRESS: W-value
    // *++: W-value
    // e) "ALF"
    // ADDRESS: characters
    // *++: characters
    // f) "END"
    // ADDRESS: W-value
    // (4:5) field is the location of the instruction at which the program
    // begins.

    // 12. Literal constants
    // A W-value that is less than 10 characters long may be enclosed between "="
    // signs and used as a future reference.


    // MIX Characters
    private static final Character[] characterCode = new Character[56];

    static {
        initCharacterCode();
    }

    private static int charToCode(char c) {
        for (int i = 0; i < characterCode.length; ++i) {
            if (characterCode[i] == c)
                return i;
        }
        throw new IllegalArgumentException("Invalid character: " + c);
    }

    private static void initCharacterCode() {
        characterCode[0] = ' ';
        characterCode[1] = 'A';
        characterCode[2] = 'B';
        characterCode[3] = 'C';
        characterCode[4] = 'D';
        characterCode[5] = 'E';
        characterCode[6] = 'F';
        characterCode[7] = 'G';
        characterCode[8] = 'H';
        characterCode[9] = 'I';
        characterCode[10] = 'Δ';
        characterCode[11] = 'J';
        characterCode[12] = 'K';
        characterCode[13] = 'L';
        characterCode[14] = 'M';
        characterCode[15] = 'N';
        characterCode[16] = 'O';
        characterCode[17] = 'P';
        characterCode[18] = 'Q';
        characterCode[19] = 'R';
        characterCode[20] = 'Σ';
        characterCode[21] = 'Π';
        characterCode[22] = 'S';
        characterCode[23] = 'T';
        characterCode[24] = 'U';
        characterCode[25] = 'V';
        characterCode[26] = 'W';
        characterCode[27] = 'X';
        characterCode[28] = 'Y';
        characterCode[29] = 'Z';
        characterCode[30] = '0';
        characterCode[31] = '1';
        characterCode[32] = '2';
        characterCode[33] = '3';
        characterCode[34] = '4';
        characterCode[35] = '5';
        characterCode[36] = '6';
        characterCode[37] = '7';
        characterCode[38] = '8';
        characterCode[39] = '9';
        characterCode[40] = '.';
        characterCode[41] = ',';
        characterCode[42] = '(';
        characterCode[43] = ')';
        characterCode[44] = '+';
        characterCode[45] = '-';
        characterCode[46] = '*';
        characterCode[47] = '/';
        characterCode[48] = '=';
        characterCode[49] = '$';
        characterCode[50] = '<';
        characterCode[51] = '>';
        characterCode[52] = '@';
        characterCode[53] = ';';
        characterCode[54] = ':';
        characterCode[55] = '\'';

        // Map<Character, Integer> charCodeMap = new HashMap<>();
        // charCodeMap.put('A', 1);
    }

}
