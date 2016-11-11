package com.parkjongeun.mixsimulator.assembler;


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

    public int[] toIntArray() {
        int[] intArray = new int[cellArray.length * Word.WORD_SIZE_IN_BYTES];

        for (int i = 0, j = 0; i < cellArray.length; ++i, j = i * Word.WORD_SIZE_IN_BYTES) {
            intArray[j] = cellArray[i].bytes[0];
            intArray[1 + j] = cellArray[i].bytes[1];
            intArray[2 + j] = cellArray[i].bytes[2];
            intArray[3 + j] = cellArray[i].bytes[3];
            intArray[4 + j] = cellArray[i].bytes[4];
            intArray[5 + j] = cellArray[i].bytes[5];
        }
        return intArray;
    }
}
