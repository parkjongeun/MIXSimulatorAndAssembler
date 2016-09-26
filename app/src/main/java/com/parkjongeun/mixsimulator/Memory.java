package com.parkjongeun.mixsimulator;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Memory {

    Word[] cell = new Word[4000];

    public final static int SIZE = 4000;


    Word get(int address) {
        return cell[address];
    }

    Memory set(Word word) {

        return this;
    }

    public int getSize() {
        return 4000;
    }
}
