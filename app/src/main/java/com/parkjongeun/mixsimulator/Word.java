package com.parkjongeun.mixsimulator;

import android.support.annotation.IntRange;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Word {

    int buf = 0;
    int[] bytes;
    public final static int BYTE_SIZE = 64;
    public final static int WORD_SIZE = 6;
    public final static int COUNT_OF_BYTES_IN_WORD = 5;
    final static int ZERO = 0;

    public final static int PLUS = 1;
    public final static int MINUS = -1;

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
    public int getField(@IntRange(from = 1, to = 5) int number) {
        return bytes[number];
    }

    public Word setField(@IntRange(from = 1, to = 5) int number, int value) {
        if (value < 0 || value > 63) {
            throw new IllegalArgumentException("value < 0 or value > 63. value: " + value);
        }
        bytes[number] = value;
        return this;
    }

    public int getSign() {
        //return buf < 0;
        return bytes[0];
    }

    public void setSign(int sign) {
        if (sign == PLUS || sign == MINUS) {
            bytes[0] = sign;
        } else {
            throw new IllegalArgumentException("sign: " + sign);
        }
    }

    public boolean isSignPlus() {
        return getSign() == PLUS;
    }

    public int getQuantity() {
        return getQuantity(0, WORD_SIZE - 1);
    }

    public int getQuantity(int left, int right) {
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

    public void setQuantity(final int sign, final int quantity) {
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

    public int getAddress() {
        //return (buf < 0 ? -1 : 1) * buf >> 12 & 0xFF;
        return (bytes[1] + bytes[2]) * (getSign() == PLUS ? 1 : -1);
    }

    public int getIndexSpec() {
        //return buf >> 8 & 0xF;
        return bytes[3];
    }

    public int getFieldSpec() {
        //return buf >> 4 & 0xF;
        return bytes[4];
    }

    public int getOpCode() {
        //return buf & 0xF;
        return bytes[5];
    }

    public void reset() {
        bytes[0] = PLUS;
        bytes[1] = 0;
        bytes[2] = 0;
        bytes[3] = 0;
        bytes[4] = 0;
        bytes[5] = 0;
    }

    public void shiftLeft() {
        bytes[1] = bytes[2];
        bytes[2] = bytes[3];
        bytes[3] = bytes[4];
        bytes[4] = bytes[5];
        bytes[5] = ZERO;
    }

    public void shiftRight() {
        bytes[5] = bytes[4];
        bytes[4] = bytes[3];
        bytes[3] = bytes[2];
        bytes[2] = bytes[1];
        bytes[1] = ZERO;
    }

    public void shiftLeft(int number) {
        for (int i = 0; i < number && i < 5; ++i) {
            shiftLeft();
        }
    }

    public void shiftRight(int number) {
        for (int i = 0; i < number && i < 5; ++i) {
            shiftRight();
        }
    }

    public void writeTo(Word word) {
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

    public void setAddress(final int sign, final int aa) {
        if (!(sign == MINUS || sign == PLUS)) {
            throw new IllegalArgumentException("The argument sign must either be MINUS or PLUS: " + sign);
        }
        if (aa < 0) {
            throw new IllegalArgumentException("The argument aa is negative: " + aa);
        }
        if (aa >= BYTE_SIZE * BYTE_SIZE) {
            throw new IllegalArgumentException("The argument aa doesn't fit in 2-bytes: " + aa);
        }
        int a1, a2;
        a1 = aa / BYTE_SIZE;
        a2 = aa % BYTE_SIZE;
        setField(1, a1);
        setField(2, a2);
        setSign(sign);
    }

    public void setI(int i) {
        setField(3, i);
    }

    public void setF(int f) {
        setField(4, f);
    }

    public void setC(int c) {
        setField(5, c);
    }
}
