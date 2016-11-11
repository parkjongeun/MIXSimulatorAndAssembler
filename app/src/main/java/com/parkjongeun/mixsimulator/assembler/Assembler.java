package com.parkjongeun.mixsimulator.assembler;

import com.parkjongeun.mixsimulator.util.Pair;

import java.util.ArrayList;
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


    static String mPgm =
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

    static String isainsPgm = "B EQU 1(4:4)\n" +
            "BMAX EQU B-1\n" +
            "UMAX EQU 20\n" +
            "TABLE NOP GOOD(BMAX)\n" +
            " ADD FLOAT(5:5)\n" +
            " SUB FLOAT(5:5)\n" +
            " MUL FLOAT(5:5)\n" +
            " DIV FLOAT(5:5)\n" +
            " HLT GOOD\n" +
            " SRC GOOD\n" +
            " MOVE MEMORY(BMAX)\n" +
            " LDA FIELD(5:5)\n" +
            " LD1 FIELD(5:5)\n" +
            " LD2 FIELD(5:5)\n" +
            " LD3 FIELD(5:5)\n" +
            " LD4 FIELD(5:5)\n" +
            " LD5 FIELD(5:5)\n" +
            " LD6 FIELD(5:5)\n" +
            " LDX FIELD(5:5)\n" +
            " LDAN FIELD(5:5)\n" +
            " LD1N FIELD(5:5)\n" +
            " LD2N FIELD(5:5)\n" +
            " LD3N FIELD(5:5)\n" +
            " LD4N FIELD(5:5)\n" +
            " LD5N FIELD(5:5)\n" +
            " LD6N FIELD(5:5)\n" +
            " LDXN FIELD(5:5)\n" +
            " STA FIELD(5:5)\n" +
            " ST1 FIELD(5:5)\n" +
            " ST2 FIELD(5:5)\n" +
            " ST3 FIELD(5:5)\n" +
            " ST4 FIELD(5:5)\n" +
            " ST5 FIELD(5:5)\n" +
            " ST6 FIELD(5:5)\n" +
            " STX FIELD(5:5)\n" +
            " STJ FIELD(5:5)\n" +
            " STZ FIELD(5:5)\n" +
            " JBUS MEMORY(UMAX)\n" +
            " IOC GOOD(UMAX)\n" +
            " IN MEMORY(UMAX)\n" +
            " OUT MEMORY(UMAX)\n" +
            " JRED MEMORY(UMAX)\n" +
            " JLE MEMORY\n" +
            " JANP MEMORY\n" +
            " J1NP MEMORY\n" +
            " J2NP MEMORY\n" +
            " J3NP MEMORY\n" +
            " J4NP MEMORY\n" +
            " J5NP MEMORY\n" +
            " J6NP MEMORY\n" +
            " JXNP MEMORY\n" +
            " ENNA GOOD\n" +
            " ENN1 GOOD\n" +
            " ENN2 GOOD\n" +
            " ENN3 GOOD\n" +
            " ENN4 GOOD\n" +
            " ENN5 GOOD\n" +
            " ENN6 GOOD\n" +
            " ENNX GOOD\n" +
            " CMPA FLOAT(5:5)\n" +
            " CMP1 FIELD(5:5)\n" +
            " CMP2 FIELD(5:5)\n" +
            " CMP3 FIELD(5:5)\n" +
            " CMP4 FIELD(5:5)\n" +
            " CMP5 FIELD(5:5)\n" +
            " CMP6 FIELD(5:5)\n" +
            " CMPX FIELD(5:5)\n" +
            "BEGIN LDA INST\n" +
            " CMPA VALID(3:3)\n" +
            " JG BAD\n" +
            " LD1 INST(5:5)\n" +
            " DEC1 64\n" +
            " J1NN BAD\n" +
            " CMPA TABLE+64,1(4:4)\n" +
            " JG BAD\n" +
            " LD1 TABLE+64,1(1:2)\n" +
            " JMP 0,1\n" +
            "FLOAT CMPA VALID(4:4)\n" +
            " JE GOOD\n" +
            "FIELD ENTA 0\n" +
            " LDX INST(4:4)\n" +
            " DIV =9=\n" +
            " STX *+1(0:2)\n" +
            " INCA 0\n" +
            " CMPA =5=\n" +
            " JG BAD\n" +
            "MEMORY LDX INST(3:3)\n" +
            " JXNZ GOOD\n" +
            " LDX INST(0:2)\n" +
            " JXN BAD\n" +
            " CMPX =3999=\n" +
            " JLE GOOD\n" +
            " JMP BAD\n" +
            "VALID CMPX 3999,6(6)\n" +
            "*VALID CON 63,3999(0:2),6(3:3),6(4:4) CMPX 3999,6(6)\n" +
            "*\n" +
            "*\n" +
            "*\n" +
            "INST CON 63,3999(0:2),6(3:3),45(4:4)\n" +
            "GOOD OUT GTEXT(18)\n" +
            " HLT\n" +
            "BAD OUT BTEXT(18)\n" +
            " HLT\n" +
            "GTEXT ALF \"GOOD \"\n" +
            "BTEXT ALF \"BAD  \"\n" +
            " END BEGIN\n";

    static String helloPgm = "* mixal hello world\n" +
            "*\n" +
            "TERM\tEQU\t18\n" +
            "\tORIG\t3000\n" +
            "START\tOUT\tMSG(TERM)\n" +
            "\tHLT\n" +
            "MSG\tALF\t\"MIXAL\"\n" +
            "\tALF\t\" HELL\"\n" +
            "\tALF\t\"O WOR\"\n" +
            "\tALF\t\"LD   \"\n" +
            "\tEND\tSTART\n";


    public Pair<int[], Integer> assemble(final String pgm) {
        final List<Line> lines = parse(pgm);

        final Memory memory = new Memory();
        int locCounter = 0;

        final SymbolTable symTable = new SymbolTableImpl();

        Map<Integer, Word> literalConstMap = new HashMap<>();


        // The assembly process depends on the value of the OP field.
        // There are six possibilities for OP:
        // Symbolic MIX operator, EQU, ORIG, CON, ALF, END
        // Process line by line
        for (int i = 0; i < lines.size(); ++i) {
            final Line line = lines.get(i);
            String op = line.colOP;

            if ("EQU".equals(line.colOP)) {
                processEQUDirective(locCounter, line, symTable);

            } else if ("ORIG".equals(line.colOP)) {
                locCounter = processORIGDirective(locCounter, line, symTable);

            } else if ("CON".equals(line.colOP)) {
                processCONDirective(locCounter, memory, line, symTable);
                locCounter++;

            } else if ("ALF".equals(line.colOP)) {
                processALFDirective(locCounter, memory, line, symTable);
                locCounter++;

            } else if ("END".equals(line.colOP)) {
                /*if (!line.colLOC.isEmpty()) {
                    symTable2.add(line.colLOC, 0);
                }*/

                locCounter = processUndefinedSymbols(symTable.getUndefinedFutureRefs(), symTable, memory, locCounter);

                locCounter = processLiteralConstant(literalConstMap, memory, locCounter);

                // TODO: BUG.
                //if (!line.colLOC.isEmpty()) {
                //    symTable2.add(line.colLOC, locCounter);
                //}

                Word w = WValue.assemble(line.colADDRESS, symTable, locCounter);
                memory.write(locCounter, w);

                processFutureRef(symTable, memory);

                final int startFrom = memory.get(locCounter).getQuantity(4, 5);
                return new Pair<>(memory.toIntArray(), startFrom);
            } else {
                processMIXOperator(locCounter, memory, line, literalConstMap, symTable);
                locCounter++;
            }
        }

        return new Pair<>(memory.toIntArray(), 0);
    }

    private void processFutureRef(SymbolTable symTable, final Memory memory) {
        Map<Integer, String> futureRefs = symTable.getFutureRef();
        Set<Integer> addresses = futureRefs.keySet();

        for (int address : addresses) {
            // Only relevant to the A-part.
            final Word word = memory.get(address);

            final String futureRef = futureRefs.get(address);

            final int value = symTable.get(futureRef);

            //if (value == null) {
            //    throw new IllegalStateException("Undefined symbol: " + futureRef);
            //}

            //final int valueAsInt = Integer.parseInt(value);

            // TODO: -0.
            final int sign = value < 0 ? Word.MINUS : Word.PLUS;

            word.setAddress(sign, Math.abs(value));
        }

    }

    private int processLiteralConstant(Map<Integer, Word> literalConstMap, final Memory memory, int locCounter) {
        final Set<Integer> addresses = literalConstMap.keySet();
        for (int address : addresses) {
            final Word word = literalConstMap.get(address);

            memory.write(locCounter, word);
            memory.get(address).setAddress(Word.PLUS, locCounter);
            locCounter++;
        }
        return locCounter;
    }

    // If the symbol never appears in LOC, a new line is effectively inserted before the END line,
    // having OP = "CON" and ADDRESS = "0" and the name of the symbol in LOC. (TAOCP1 p. 156)
    private int processUndefinedSymbols(List<String> undefs, SymbolTable symTable, final Memory memory, int locCounter) {
        for (String symbol : undefs) {
            memory.write(locCounter, new Word());
            symTable.add(symbol, locCounter);
            locCounter++;
        }
        return locCounter;
    }

    private void processCONDirective(final int locCounter, Memory memory, Line line, SymbolTable symTable) {
        if (!"CON".equals(line.colOP)) {
            throw new IllegalArgumentException("OP value must be 'CON': " + line.colOP);
        }

        final String symbol = line.colLOC;

        Word w = WValue.assemble(line.colADDRESS, symTable, locCounter);
        memory.write(locCounter, w);

        if (!symbol.isEmpty()) {
            symTable.add(symbol, locCounter);
        }
    }

    private void processALFDirective(int locCounter, Memory memory, Line line, SymbolTable symTable) {
        if (!"ALF".equals(line.colOP)) {
            throw new IllegalArgumentException("OP value must be 'ALF': " + line.colOP);
        }

        final String loc = line.colLOC;
        final String address = line.colADDRESS;

        final Word word = new Word();

        if (address.length() > Word.COUNT_OF_BYTES_IN_WORD) {
            throw new IllegalArgumentException("Too many characters: " + address);
        }

        for (int i = 0; i < address.length(); ++i) {
            final int code = charToCode(address.charAt(i));
            word.setField(1 + i, code);
        }

        memory.write(locCounter, word);

        if (!loc.isEmpty()) {
            symTable.add(loc, locCounter);
        }
    }

    int processORIGDirective(int locCounter, Line l, SymbolTable symTable) {
        String symbol = l.colLOC;
        if (!symbol.isEmpty()) {
            symTable.add(symbol, locCounter);
        }
        Word w = WValue.assemble(l.colADDRESS, symTable, locCounter);
        return w.getQuantity();
    }

    void processEQUDirective(int locCounter, Line line, SymbolTable symTable) {
        String symbol = line.colLOC;
        if (!symbol.isEmpty()) {
            Word w = WValue.assemble(line.colADDRESS, symTable, locCounter);
            symTable.add(symbol, w.getQuantity());
        }
    }

    static void tokenizeIntoAIF(String str, String[] outToken) {
        int comma = str.indexOf(',');
        int lparen = str.indexOf('(');
        String a, i, f;
        if (comma != -1) {
            a = str.substring(0, comma);
            if (lparen != -1) {
                i = str.substring(comma, lparen);
                f = str.substring(lparen);
            } else {
                i = str.substring(comma);
                f = "";
            }
        } else {
            if (lparen != -1) {
                a = str.substring(0, lparen);
                i = "";
                f = str.substring(lparen);
            } else {
                a = str;
                i = "";
                f = "";
            }
        }
        outToken[0] = a;
        outToken[1] = i;
        outToken[2] = f;
    }

    void processMIXOperator(int locCounter, Memory memory, Line line, Map<Integer, Word> literalConstMap, SymbolTable symTable) {
        String symbol = line.colLOC;
        if (!symbol.isEmpty()) {
            symTable.add(symbol, locCounter);
        }

        // Parse AIF
        // STX 0,4(1:4)
        // LD4 24,4
        // ST2 PRIME+L,1
        final String[] outToken = new String[3];
        tokenizeIntoAIF(line.colADDRESS, outToken);
        final String a = outToken[0];
        final String i = outToken[1];
        final String f = outToken[2];

        // TODO: Correct the sign. In case of -0.
        int valA = APart.eval(a, symTable, locCounter, literalConstMap);
        int valI = IndexPart.eval(i, symTable, locCounter);

        OpCode opCode = null;
        try {
             opCode = OpCode.valueOf(line.colOP);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(locCounter + " Invalid MIX OP Symbol: " + line.colOP);
        }

        int valF = FPart.eval(f, symTable, locCounter, opCode.fieldSpec);

        Word w = new Word();
        w.setAddress(valA < 0 ? Word.MINUS : Word.PLUS, Math.abs(valA));
        w.setI(valI);
        w.setF(valF);
        w.setC(opCode.code);

        memory.write(locCounter, w);
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
