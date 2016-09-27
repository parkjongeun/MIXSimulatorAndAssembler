package com.parkjongeun.mixsimulator;

import android.support.annotation.IntRange;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Word {

    int buf = 0;
    int[] bytes;
    final static int BYTE_SIZE = 64;
    final static int WORD_SIZE = 6;
    final static int ZERO = 0;

    final static int PLUS = 1;
    final static int MINUS = -1;

    public static int MAX_VALUE = Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE - 1;
    public static int MIN_VALUE = -(Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE - 1);

    private static final int weight[] = {
            1,
            Word.BYTE_SIZE,
            Word.BYTE_SIZE * Word.BYTE_SIZE,
            Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE,
            Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE
    };

    public Word() {
        bytes = new int[WORD_SIZE];
        bytes[0] = PLUS;
        for (int i = 1; i < WORD_SIZE; ++i) {
            bytes[i] = 0;
        }
    }

    @IntRange(from = 0, to = 63)
    int getField(@IntRange(from = 1, to = 5) int number) {
        return bytes[number];
    }

    Word setField(@IntRange(from = 1, to = 5) int number, int value) {
        if (value < 0 || value > 63) {
            throw new IllegalArgumentException("value < 0 or value > 63. value: " + value);
        }
        bytes[number] = value;
        return this;
    }

    int getSign() {
        //return buf < 0;
        return bytes[0];
    }

    void setSign(int sign) {
        bytes[0] = sign;
    }


    int getQuantity() {
        return getQuantity(0, WORD_SIZE - 1);
    }

    int getQuantity(int left, int right) {
        int sign = 1;
        int quantity = 0;
        if (left == 0) {
            if (getSign() == MINUS) {
                sign = -1;
            }
            ++left;
        }
        for (int i = right, w = 0; i >= left; --i, ++w) {
            quantity += bytes[i] * weight[w];
        }
        if (sign != 1) {
            quantity *= sign;
        }
        return quantity;
    }

/*
    @Deprecated
    void setQuantity(final int quantity) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity is 0.");
        }
        // +0 is default.
        setQuantity(quantity < 0 ? MINUS : PLUS, quantity);
    }*/

    void setQuantity(final int sign, final int quantity) {
        if (Math.abs(quantity) > MAX_VALUE) {
            throw new IllegalArgumentException("" + quantity);
        }
        reset();
        for (int i = WORD_SIZE - 1, q = Math.abs(quantity); i > 0 && q > 0; --i, q /= BYTE_SIZE) {
            int n = q % BYTE_SIZE;
            setField(i, n);
        }
        setSign(sign);
    }

    int getAddress() {
        //return (buf < 0 ? -1 : 1) * buf >> 12 & 0xFF;
        return (bytes[1] + bytes[2]) * (getSign() == PLUS ? 1 : -1);
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

    void reset() {
        bytes[0] = PLUS;
        bytes[1] = 0;
        bytes[2] = 0;
        bytes[3] = 0;
        bytes[4] = 0;
        bytes[5] = 0;
    }

    void shiftLeft() {
        bytes[1] = bytes[2];
        bytes[2] = bytes[3];
        bytes[3] = bytes[4];
        bytes[4] = bytes[5];
        bytes[5] = ZERO;
    }

    void shiftRight() {
        bytes[5] = bytes[4];
        bytes[4] = bytes[3];
        bytes[3] = bytes[2];
        bytes[2] = bytes[1];
        bytes[1] = ZERO;
    }

    void shiftLeft(int number) {
        for (int i = 0; i < number && i < 5; ++i) {
            shiftLeft();
        }
    }

    void shiftRight(int number) {
        for (int i = 0; i < number && i < 5; ++i) {
            shiftRight();
        }
    }

    void copy(Word word) {
        for (int i = 0; i < WORD_SIZE; ++i) {
            word.bytes[i] = bytes[i];
        }
        /*word.setSign(getSign());
        word.setField(1, getField(1));
        word.setField(2, getField(2));
        word.setField(3, getField(3));
        word.setField(4, getField(4));
        word.setField(5, getField(5));*/
    }
}
