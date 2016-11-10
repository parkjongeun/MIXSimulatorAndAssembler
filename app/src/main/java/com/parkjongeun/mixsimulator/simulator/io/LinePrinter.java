package com.parkjongeun.mixsimulator.simulator.io;

import com.parkjongeun.mixsimulator.Word;

/**
 * Created by Parkjongeun on 10/11/2016.
 */

public class LinePrinter extends IO {

    private int mPage = 1;
    private int mLine = 1;

    public LinePrinter(Thread mixThread) {
        super(mixThread);
    }

    @Override
    protected Word[] in() {
        throw new IllegalArgumentException("This is a input device.");
    }

    @Override
    protected void out(Word[] block) {
        if (block.length != blockSize()) {
            throw new IllegalArgumentException();
        }
        char[] line = new char[24 * 5];

        for (int i = 0; i < block.length; ++i) {
            Word w = block[i];
            line[0 + i * 5] = codeToChar(w.getField(1));
            line[1 + i * 5] = codeToChar(w.getField(2));
            line[2 + i * 5] = codeToChar(w.getField(3));
            line[3 + i * 5] = codeToChar(w.getField(4));
            line[4 + i * 5] = codeToChar(w.getField(5));
        }
        //System.out.print("LINE " + mLine + ": ");
        System.out.println(line);
        mLine++;
        try {
            // Simulate the speed of line printer.
            Thread.sleep(250);
        } catch (InterruptedException e) {

        }
    }

    @Override
    protected void ioc(int m) {
        if (m != 0) {
            throw new IllegalArgumentException();
        }
        ++mPage;
        mLine = 0;
    }

    @Override
    public int blockSize() {
        return 24; // 24 words
    }

    @Override
    protected boolean isInputDevice() {
        return false;
    }


    // MIX Characters
    private static final Character[] characterCode = new Character[56];

    static {
        initCharacterCode();
    }

    private static char codeToChar(int code) {
        if (code < 0) {
            throw new IllegalArgumentException();
        }
        if (code >= 0 && code < characterCode.length) {
            return characterCode[code];
        }
        return '?';
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
