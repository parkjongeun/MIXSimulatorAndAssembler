
package com.parkjongeun.mixsimulator;


import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */

public class Mix {

    Register mRegA;
    Register mRegX;

    IndexRegister[] mRegIx;

    IndexRegister mRegJ;

    OverFlowToggle mOverFlowToggle;
    CompIndicator mCompIndicator;

    Memory mMemory;

    IO[] mIOUnit;

    int mPC = 0;

    final static int ZERO = 0;
    final static int BYTE_COUNT = 5;



    // Generic load operation.
    void load(Register reg, int addr, int left, int right) {
        checkFieldSpec(left, right);
        checkAddress(addr);


        reg.reset();

        for (int i = left; i <= right; ++i) {
            reg.setField(i, mMemory.get(addr).getField(i));
        }
        if (right > 0) {
            reg.shiftRight(BYTE_COUNT - right);
        }
    }

    // Generic negative load operation.
    void loadNegative(Register reg, int addr, int left, int right) {
        load(reg, addr, left, right);
        if (left == 0) {
            reg.setSign(-mMemory.get(addr).getSign());
        }
    }


    // STA
    // A portion of the contents of rA replaces the field of CONTENTS(M) specified by F.
    // The other parts of CONTENT(M) are unchanged.
    void storeRA(int addr, int left, int right) {
        store(mRegA, addr, left, right);
    }

    // STX
    void storeRX(int addr, int left, int right) {
        store(mRegX, addr, left, right);
    }

    // STi
    void storeRIx(int rI, int addr, int left, int right) {
        store(mRegIx[rI], addr, left, right);
    }

    // STJ
    // Same as STi, except that rJ is stored and its sign is always +.
    void storeJ(int addr, int left, int right) {
        store(mRegJ, addr, left, right);
        mMemory.get(addr).setSign(Word.PLUS);
    }

    // STZ
    // Same as STA, except that plus zero is stored.
    void storeZero(int addr, int left, int right) {
        store(mRegA, addr, left, right);
        mMemory.get(addr).setSign(Word.PLUS);
    }

    // Generic store operation.
    void store(Register reg, int addr, int left, int right) {
        checkFieldSpec(left, right);
        checkAddress(addr);

        Word word = mMemory.get(addr);

        for (int f = right, s = BYTE_COUNT; f >= left && f > 0; --f, --s) {
            word.setField(f, reg.getField(s));
        }
        if (left == 0) {
            word.setSign(reg.getSign());
        }
    }

    void checkFieldSpec(int left, int right) {
        if (left > right) {
            throw new IllegalArgumentException("left > right");
        }
        if (left < 0 || left > 5 || right < 0 || right > 5) {
            throw new IndexOutOfBoundsException();
        }
    }

    void checkAddress(int addr) {
        if (addr < 0 || addr >= mMemory.getSize()) {
            throw new IndexOutOfBoundsException();
        }
    }




    // SLA
    void shiftLeftRegA(int number) {
        mRegA.shiftLeft(number);
    }

    // SRA
    void shiftRightRegA(int number) {
        mRegA.shiftRight(number);
    }

    // SLAX
    void shiftLeftRegAX() {
        mRegA.shiftLeft();
        mRegA.setField(5, mRegX.getField(1));
        mRegX.shiftLeft();
    }

    // SRAX
    void shiftRightRegAX() {
        mRegX.shiftRight();
        mRegX.setField(1, mRegA.getField(5));
        mRegA.shiftRight();
    }

    // SLC
    void shiftLeftRegAXCircle() {
        int tmp = mRegA.getField(1);
        shiftLeftRegAX();
        mRegX.setField(5, tmp);
    }

    // SRC
    void shiftRightRegAXCircle() {
        int tmp = mRegX.getField(5);
        shiftRightRegAX();
        mRegA.setField(1, tmp);
    }


    // Arithmetic operators

    // ADD
    // If the result is zero, the sign of rA is unchanged.
    void add(int addr, int left, int right) {
        int quantity = mMemory.get(addr).getQuantity(left, right);
        add(quantity);
    }

    void add(int quantity) {
        add(mRegA, quantity);
    }

    void add(Register reg, int quantity) {
        int r = reg.getQuantity();

        int sum = r + quantity;
        if (sum > Word.MAX_VALUE || sum < Word.MIN_VALUE) {
            mOverFlowToggle.setOverFlow(true);
        }
        int sign = sum == 0 ? reg.getSign() : (sum < 0 ? Word.MINUS : Word.PLUS);
        reg.setQuantity(sign, sum);
    }

    void subtract(int addr, int left, int right) {
        int quantity = mMemory.get(addr).getQuantity(left, right);
        add(-quantity);
    }

    // The 10-byte product, V times rA, replaces register A and X.
    // The signs of rA and rX are both set to the algebraic sign of the product.
    void multiply(int addr, int left, int right) {

        long mem = mMemory.get(addr).getQuantity(left, right);
        long rA = mRegA.getQuantity();
        long mul = mem * rA;

        long mulAbs = Math.abs(mul);

        int ls = (int) mulAbs % Word.MAX_VALUE;
        int gs = (int) mulAbs / Word.MAX_VALUE;
        int sign = mMemory.get(addr).getSign() == mRegA.getSign() ? Word.PLUS : Word.MINUS;
        mRegA.setQuantity(sign, gs);
        mRegX.setQuantity(sign, ls);
    }

    // The value of rA and rX, treated as a 10byte-number rAX with the sign of rA, is divided by the value V.
    // If V = 0 or if the quotient is more than five bytes in magnitude (this is equivalent to the condition that |rA| >= |V|,
    // registers A and X are filled with undefined information and the overflow toggle is set on.
    // Otherwise the quotient +-floor(|aAX/V|) is placed in rA and the remainder +-(|rAX| mod |V|) is placed in rX.
    // The sign of rA afterwards is the algebraic sign of the quotient.
    // The sign of rX afterwards is the previous sign of rA.
    void divide(int addr, int left, int right) {
        long rAAbs = Math.abs(mRegA.getQuantity());
        long rXAbs = Math.abs(mRegX.getQuantity());

        long v = mMemory.get(addr).getQuantity(left, right);
        int vSign = mMemory.get(addr).getSign();

        if (rAAbs >= Math.abs(v) || v == 0) {
            mOverFlowToggle.setOverFlow(true);
        } else {
            long rAX = (mRegA.getSign() == Word.MINUS ? -1 : 1) * rAAbs * (Word.MAX_VALUE + 1) + rXAbs;
            long quotient = rAX / v; // +-floor(|aAX/V|)
            long remainder = (mRegA.getSign() == Word.MINUS ? -1 : 1) * Math.abs(rAX) % Math.abs(v); // +-(|rAX| mod |V|)
            mRegA.setQuantity(mRegA.getSign() == vSign ? Word.PLUS : Word.MINUS, (int) Math.abs(quotient));
            mRegX.setQuantity(mRegA.getSign(), (int) remainder);
        }
    }

    // Address transfer operators.

    // The quantity M is loaded into rA. If M = 0, the sign of the instruction is loaded.
    void enter(Register reg, int sign, int addr, int index) {
        if (sign != Word.PLUS && sign != Word.MINUS) {
            throw new IllegalArgumentException();
        }
        int m = calcM(sign, addr, index);

        int rSign = m == 0 ? sign : (m < 0 ? Word.MINUS : Word.PLUS);
        reg.setQuantity(rSign, m);
    }

    void enter(Register reg, int sign, int addr) {
        enter(reg, sign, addr, 0);
    }

    void enterNegative(Register reg, int sign, int addr, int index) {
        enter(reg, sign, addr, index);
        reg.setSign(reg.getSign() == Word.MINUS ? Word.PLUS : Word.MINUS);
    }

    void enterNegative(Register reg, int sign, int addr) {
        enterNegative(reg, sign, addr, 0);
    }

    void increase(Register reg, int addr, int index) {
        int m = calcM(addr, index);
        add(reg, m);
    }

    void decrease(Register reg, int addr, int index) {
        int m = calcM(addr, index);
        add(reg, -m);
    }

    int calcM(int addr, int index) {
        int indexValue = index == 0 ? 0 : mRegIx[index].getQuantity();
        int m = addr + indexValue;
        return m;
    }

    int calcM(int sign, int addr, int index) {
        return calcM((sign == Word.MINUS ? -1 : 1) * Math.abs(addr), index);
    }


    // Comparison operators.

    // The specified field of rA is compared with the same field of CONTENTS(M).
    // If F does not include the sign position, the fields are both considered nonnegative;
    // otherwise the sign is taken into account in the comparison.
    // (An equal comparison always occures when F is (0:0), since minus zero equals plus zero.)

    void compare(Register reg, int addr, int left, int right) {
        int regValue = reg.getQuantity(left, right);
        int memValue = mMemory.get(addr).getQuantity(left, right);

        if (regValue == memValue) {
            mCompIndicator.setEqual();
        } else if (regValue < memValue) {
            mCompIndicator.setLess();
        } else if (regValue > memValue) {
            mCompIndicator.setGreater();
        }
    }

    void jump(int from, int to) {
        checkMemoryBound(from);
        checkMemoryBound(to);

        mRegJ.setQuantity(Word.PLUS, from + 1);
        jumpSaveJ(to);
    }

    void jumpSaveJ(int to) {
        checkMemoryBound(to);
        mPC = to;
    }

    void checkMemoryBound(int addr) {
        if (addr < 0
                || addr >= Memory.SIZE) {
            throw new IllegalArgumentException();
        }
    }

    void jumpOnOverflow(int from, int to) {
        if (mOverFlowToggle.isOverFlow()) {
            mOverFlowToggle.setOverFlow(false);
            jump(from, to);
        }
    }

    void jumpOnNoOverflow(int from, int to) {
        if (!mOverFlowToggle.isOverFlow()) {
            jump(from, to);
        } else {
            mOverFlowToggle.setOverFlow(false);
        }
    }

    void jumpOnLess(int from, int to) {
        if (mCompIndicator.isLess()) {
            jump(from, to);
        }
    }

    void jumpOnEqual(int from, int to) {
        if (mCompIndicator.isEqual()) {
            jump(from, to);
        }
    }

    void jumpOnGreater(int from, int to) {
        if (mCompIndicator.isGreater()) {
            jump(from, to);
        }
    }

    void jumpOnGreaterOrEqual(int from, int to) {
        if (mCompIndicator.isGreater() || mCompIndicator.isEqual()) {
            jump(from, to);
        }
    }

    void jumpOnUnEqual(int from, int to) {
        if (mCompIndicator.isLess() || mCompIndicator.isGreater()) {
            jump(from, to);
        }
    }

    void jumpOnLessOrEqual(int from, int to) {
        if (mCompIndicator.isLess() || mCompIndicator.isEqual()) {
            jump(from, to);
        }
    }

    void jumpNegative(Register reg, int from, int to) {
        int quantity = reg.getQuantity();
        if (quantity < 0) {
            jump(from, to);
        }
    }

    void jumpZero(Register reg, int from, int to) {
        int quantity = reg.getQuantity();
        if (quantity == 0) {
            jump(from, to);
        }
    }

    void jumpPositive(Register reg, int from, int to) {
        int quantity = reg.getQuantity();
        if (quantity > 0) {
            jump(from, to);
        }
    }

    void jumpNonnegative(Register reg, int from, int to) {
        int quantity = reg.getQuantity();
        if (!(quantity < 0)) {
            jump(from, to);
        }
    }

    void jumpNonzero(Register reg, int from, int to) {
        int quantity = reg.getQuantity();
        if (quantity != 0) {
            jump(from, to);
        }
    }

    void jumpNonpositive(Register reg, int from, int to) {
        int quantity = reg.getQuantity();
        if (!(quantity > 0)) {
            jump(from, to);
        }
    }

    // MOVE
    // The number of words specified by F is moved, starting from location M to the location specified by the contents of index register 1.
    // The transfer occurs one word at a time, and rI1 is increased by the value of F at the end of the operation.
    // If F = 0, nothing happens.
    void move(int addr, int amount) {
        int rI1 = mRegIx[1].getQuantity();
        if (rI1 < 0) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < amount; ++i) {
            mMemory.get(rI1).copy(mMemory.get(addr));
            ++rI1;
            ++addr;
        }
    }


    /**
     * Input-output operators.
     * MIX has a fair amount of input-output equipment.
     * Each device is given a number as follows:
     *
     * 0-7 | Tape | 100 words
     * 8-15 | Disk or drum | 100 words
     * 16 | Card reader | 16 words
     * 17 | Card punch | 16 words
     * 18 | Line printer | 24 words
     * 19 | Typewriter terminal | 14 words
     * 20 | Paper tape | 14 words
     *
     * Input ior output with magnetic tape, disk, or drum units reads or writes full words (five bytes and a sign).
     * Input or output with units 16 through 20, however is always done in a character code where each byte represents one alphameric character.
     *
     * When character-code input is being done, the signs of all words of all words are set to +; on output, signs are ignored.
     * If a typewriter is used for input, the "carrage return" that is typed at the end each line causes the remainder of that line to be filled with blacks.
     *
     * The disk and drum units are external memory devices each containing 100-word blocks.
     * On every IN, OUT, or IOC instruction as defined below, the particular 100-word block referred to by the instruction is specified by the current contents of rX,
     * which should not exceed the capacity of the disk or drum involved.
     *
     */

    // IN (input). C = 36; F = unit.
    // This instruction initiates the transfer of information from the input unit specified into consecutive locations starting with M.

    // OUT (output). C = 37; F = unit.
    // This instruction starts the transfer of information from memory locations starting at M to the output unit specified.

    // IOC (input-output control). C = 35; F = unit.
    // Disk or drum: M should be zero. The effect is to position the device according to rX so that the next IN or OUT operation on this unit will take less time
    // if is uses the same rX setting.


    void input(final int addr, final int unit) {
        // Blocking
        mIOUnit[unit].waitUntilReady();

        // TODO: Async
        Word[] block = mIOUnit[unit].input();
        for (int i = 0; i < block.length; ++i) {
            mMemory.get(addr + i).copy(block[i]);
        }
    }

    void output(int addr, int unit) {
        // Blocking
        mIOUnit[unit].waitUntilReady();

        // TODO: Async
        int blockSize = mIOUnit[unit].blockSize();

        Word[] block = new Word[blockSize];

        for (int i = 0; i < blockSize; ++i) {
            block[i].copy(mMemory.get(addr + i));
        }
        mIOUnit[unit].output(block);
    }

    void iocontrol(int addr, int unit) {
        mIOUnit[unit].iOControl(addr);
    }

    void jumpReady(int from, int to, int unit) {
        if (mIOUnit[unit].isReady()) {
            jump(from, to);
        }
    }

    void jumpBusy(int from, int to, int unit) {
        if (!mIOUnit[unit].isReady()) {
            jump(from, to);
        }
    }


    /**
     *
     * Conversion Operators.
     *
     */

    // NUM
    // This operation is used to change the character code into numeric code.
    // M is ignored.
    // Registers A and X are assumed to contain a 10-byte number in character code;
    // the NUM instruction sets the magnitude of rA equal to the numerical value of this number (treated as a decimal number).
    // The value of rX and the sign of rA are unchanged.
    // Bytes 00, 10, 20, 30, 40, ... convert to the digit zero;
    // bytes 01, 11 21, ... convert to the digit one; etc.
    // Overflow is possible, and in this case the remainder modulo b^5 is retained, where b is the byte size.
    void convertToNumeric() {
        int radix = 10;
        /*int weight[] = {
                1,
                radix * radix,
                radix * radix * radix,
                radix * radix * radix * radix,
                radix * radix * radix * radix * radix,
                radix * radix * radix * radix * radix * radix,
                radix * radix * radix * radix * radix * radix * radix,
                radix * radix * radix * radix * radix * radix * radix * radix,
                radix * radix * radix * radix * radix * radix * radix * radix * radix,
                radix * radix * radix * radix * radix * radix * radix * radix * radix * radix
        };

        int rA1 = (mRegA.getField(1) % radix) * weight[9];
        int rA2 = (mRegA.getField(2) % radix) * weight[8];
        int rA3 = (mRegA.getField(3) % radix) * weight[7];
        int rA4 = (mRegA.getField(4) % radix) * weight[6];
        int rA5 = (mRegA.getField(5) % radix) * weight[5];

        int rX1 = (mRegX.getField(1) % radix) * weight[4];
        int rX2 = (mRegX.getField(2) % radix) * weight[3];
        int rX3 = (mRegX.getField(3) % radix) * weight[2];
        int rX4 = (mRegX.getField(4) % radix) * weight[1];
        int rX5 = (mRegX.getField(5) % radix) * weight[0];

        long number = rA1 + rA2 + rA3 + rA4 + rA5 + rX1 + rX2 + rX3 + rX4 + rX5;*/

        char[] digits = new char[10];

        for (int i = 1; i <= 5; ++i) {
            digits[i - 1] = Integer.toString(mRegA.getField(i) % radix).charAt(0);
            digits[4 + i] = Integer.toString(mRegX.getField(i) % radix).charAt(0);
        }

        long numeric = Integer.parseInt(String.valueOf(digits));

        int sign = mRegA.getSign();
        if (numeric > Word.MAX_VALUE) {
            mOverFlowToggle.setOverFlow(true);
            numeric = numeric % Word.MAX_VALUE;
        }
        mRegA.setQuantity(sign, (int) numeric);
    }

    // This operation is used to change numeric code into character code suitable for output to punched cards or tape or the line printer.
    // The value in rA is converted into a 10-byte decimal number that is put into registers A and X in character code.
    // The signs of rA and rX are unchanged. M is ignored.
    void convertToCharacters() {
        int rAAbs = Math.abs(mRegA.getQuantity());

        char[] digits = Integer.toString(rAAbs, 10).toCharArray();

        int[] buf = new int[10];
        for (int i = 0; i < buf.length; ++i) {
            buf[i] = 0;
        }

        for (int i = digits.length - 1, f = 9; i >= 0; --i, --f) {
            buf[f] = Integer.parseInt(String.valueOf(digits[i]));
        }
        for (int i = 1; i <= 5; ++i) {
            mRegA.setField(i, 30 + buf[i - 1]);
            mRegX.setField(i, 30 + buf[4 + i]);
        }
    }












}
