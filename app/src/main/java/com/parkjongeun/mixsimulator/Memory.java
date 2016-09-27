package com.parkjongeun.mixsimulator;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Memory {

    Word[] cell;

    public final static int SIZE = 4000;

    public Memory() {
        cell = new Word[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            cell[i] = new Word();
        }
    }

    Word get(int address) {
        return cell[address];
    }

    public int getSize() {
        return 4000;
    }
}
