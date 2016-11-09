package com.parkjongeun.mixsimulator;

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
}
