package com.parkjongeun.mixsimulator;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Memory {

    Word[] cell = new Word[4000];

    Word get(int address) {
        return cell[address];
    }

    Memory setCell(Word word) {

        return this;
    }
}
