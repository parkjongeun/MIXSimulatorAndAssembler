package com.parkjongeun.mixsimulator;

import android.support.annotation.IntRange;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Word {

    int buf = 0;
    int[] bytes = new int[6];
    final static int BYTE_SIZE = 64;


    @IntRange(from = 0, to = 63)
    int getField(@IntRange(from = 1, to = 5) int number) {
        return bytes[number];
    }

    Word setField(@IntRange(from = 1, to = 5) int number, int value) {
        if (value < 0 || value > 63) {
            throw new IllegalArgumentException("value < 0 or value > 63");
        }
        bytes[number] = value;
        return this;
    }

    boolean getSign() {
        //return buf < 0;
        return bytes[0] != 0;
    }

    int getAddress() {
        //return (buf < 0 ? -1 : 1) * buf >> 12 & 0xFF;
        return (bytes[1] + bytes[2]) * (getSign() ? 1 : -1);
    }

    int getIndexSpec() {
        //return buf >> 8 & 0xF;
        return bytes[3];
    }

    int getFieldSpec() {
        //return buf >> 4 & 0xF;
        return bytes[4];
    }

    int getOpCode() {
        //return buf & 0xF;
        return bytes[5];
    }

    void clear() {
        bytes[0] = 0;
        bytes[1] = 0;
        bytes[2] = 0;
        bytes[3] = 0;
        bytes[4] = 0;
        bytes[5] = 0;
    }
}
