package com.parkjongeun.mixsimulator.mix;

import android.support.annotation.IntRange;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class Word extends MIXWord {

    int[] bytes;

    public final static int BYTE_SIZE = 100;

    public final static int WORD_SIZE_IN_BYTES = 6;
    public final static int COUNT_OF_BYTES_IN_WORD = 5;

    private final static int ZERO = 0;

    public final static int PLUS = 1;
    public final static int MINUS = -1;

    public static long MAX_VALUE = ((long) Math.pow(Word.BYTE_SIZE, COUNT_OF_BYTES_IN_WORD)) - 1;
    public static long MAX_ABS_VALUE = ((long) Math.pow(Word.BYTE_SIZE, COUNT_OF_BYTES_IN_WORD)) - 1;
    public static long MIN_VALUE = -(((long) Math.pow(Word.BYTE_SIZE, COUNT_OF_BYTES_IN_WORD)) - 1);


    private static final int BYTE_1 = 1;
    private static final int BYTE_2 = 2;
    private static final int BYTE_3 = 3;
    private static final int BYTE_4 = 4;
    private static final int BYTE_5 = 5;

    private static final long WEIGHT[] = {
            1,
            Word.BYTE_SIZE,
            (long) Math.pow(Word.BYTE_SIZE, 2),
            (long) Math.pow(Word.BYTE_SIZE, 3),
            (long) Math.pow(Word.BYTE_SIZE, 4),
    };

    /*static {
        WEIGHT = new long[COUNT_OF_BYTES_IN_WORD];
        for (int i = 0; i < COUNT_OF_BYTES_IN_WORD; ++i) {
            WEIGHT[i] = (long) Math.pow(Word.BYTE_SIZE, i);
        }
    }*/

    public Word() {
        bytes = new int[WORD_SIZE_IN_BYTES];
        bytes[0] = PLUS;
        bytes[1] = 0;
        bytes[2] = 0;
        bytes[3] = 0;
        bytes[4] = 0;
        bytes[5] = 0;
        //for (int i = 1; i < WORD_SIZE_IN_BYTES; ++i) {
        //    bytes[i] = 0;
        //}
    }

    public static int getSizeOfByte() {
        return BYTE_SIZE;
    }

    public int getField(@IntRange(from = 1, to = 5) int position) {
        if (!(position >= 1 && position <= 5)) {
            throw new IllegalArgumentException("!(position >= 1 && position <= 5): " + position);
        }
        return bytes[position];
    }

    public Word setField(@IntRange(from = 1, to = 5) int position, int value) {
        if (!(position >= 1 && position <= 5)) {
            throw new IllegalArgumentException("!(position >= 1 && position <= 5): " + position);
        }
        if (value < 0 || value >= BYTE_SIZE) {
            throw new IllegalArgumentException("value < 0 or value >= BYTE_SIZE. value: " + value);
        }
        bytes[position] = value;
        return this;
    }

    public int getSign() {
        return bytes[0];
    }

    public void setSignToPlus() {
        bytes[0] = PLUS;
    }

    public void setSignToMinus() {
        bytes[0] = MINUS;
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

    // NOTE: If the value is -0, 0 is returned.
    public long getQuantity() {
        return getQuantity(0, 5);
    }

    // NOTE: If the value is -0, 0 is returned.
    // On all operations where a partial field is used as an input, the sign is used if it is a part of the field,
    // otherwise the sign + is understood.
    public long getQuantity(int left, int right) {
        long absValue = 0;
        for (int i = right, w = 0; i >= 1 && i >= left; --i, ++w) {
            absValue += bytes[i] * WEIGHT[w];
        }

        if (left == 0) {
            if (bytes[0] == MINUS)
                return -absValue;
            else
                return absValue;
        } else {
            return absValue;
        }
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


    public void setQuantity(final int sign, final long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity is negative.");
        }
        if (quantity > MAX_VALUE) {
            throw new IllegalArgumentException("Doesn't fit in a word: " + quantity);
        }
        reset();

        setSign(sign);

        long q = quantity;
        for (int i = 5; i > 0 && q > 0; --i, q /= BYTE_SIZE) {
            int n = (int) (q % BYTE_SIZE);
            setField(i, n);
        }
    }

    public void setQuantitySignUnchanged(final long abs) {
        if (abs < 0) {
            throw new IllegalArgumentException("abs is negative.");
        }
        if (abs > MAX_ABS_VALUE) {
            throw new IllegalArgumentException("Doesn't fit in a word: " + abs);
        }
        resetBytes();
        long q = abs;
        for (int i = 5; i > 0 && q > 0; --i, q /= BYTE_SIZE) {
            int n = (int) (q % BYTE_SIZE);
            setField(i, n);
        }
    }

    void setBytes(final long value) {

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

    public void resetBytes() {
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
        for (int i = 0; i < WORD_SIZE_IN_BYTES; ++i) {
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
