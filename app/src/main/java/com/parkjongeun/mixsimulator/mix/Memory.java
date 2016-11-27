package com.parkjongeun.mixsimulator.mix;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Memory {

    private final Word[] cellArray;

    public final static int SIZE = 4000;

    public Memory() {
        cellArray = new Word[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            cellArray[i] = new Word();
        }
    }

    public Word get(int address) {
        return cellArray[address];
    }

    public int getSize() {
        return 4000;
    }

    public void write(int address, Word word) {
        word.writeTo(cellArray[address]);
    }

    public Word read(int address) {
        Word w = new Word();
        cellArray[address].writeTo(w);
        return w;
    }

    public static Memory fromIntArray(int[] intArray) {
        if (intArray.length != SIZE * Word.WORD_SIZE_IN_BYTES) {
            throw new IllegalArgumentException("Inconsistent.");
        }

        Memory mem = new Memory();
        for (int i = 0, j = 0; i < SIZE; ++i, j = i * Word.WORD_SIZE_IN_BYTES) {
            mem.cellArray[i].bytes[0] = intArray[j];
            mem.cellArray[i].bytes[1] = intArray[j + 1];
            mem.cellArray[i].bytes[2] = intArray[j + 2];
            mem.cellArray[i].bytes[3] = intArray[j + 3];
            mem.cellArray[i].bytes[4] = intArray[j + 4];
            mem.cellArray[i].bytes[5] = intArray[j + 5];
        }
        return mem;
    }
}
