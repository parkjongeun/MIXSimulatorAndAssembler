
package com.parkjongeun.mixsimulator.mix;


import android.util.Pair;

import com.parkjongeun.mixsimulator.mix.io.IO;
import com.parkjongeun.mixsimulator.mix.io.LinePrinter;

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

    public
    Memory mMemory;

    private IO[] mIOUnit = new IO[1 + UNIT_NUMBER_PAPER_TYPE];

    int mPC = 0;

    private final static int ZERO = 0;
    private final static int BYTE_COUNT = 5;


    private final static int UNIT_NUMBER_TAPE_MIN = 0;
    private final static int UNIT_NUMBER_TAPE_MAX = 7;
    private final static int UNIT_NUMBER_DISK_OR_DRUM_MIN = 8;
    private final static int UNIT_NUMBER_DISK_OR_DRUM_MAX = 15;
    private final static int UNIT_NUMBER_CARD_READER = 16;
    private final static int UNIT_NUMBER_CARD_PUNCH = 17;
    private final static int UNIT_NUMBER_LINE_PRINTER = 18;
    private final static int UNIT_NUMBER_TYPEWRITER_TERMINAL = 19;
    private final static int UNIT_NUMBER_PAPER_TYPE = 20;



    public Mix() {
        mRegA = new Register();
        mRegX = new Register();
        mRegIx = new IndexRegister[6 + 1];
        for (int i = 1; i <= 6; ++i) {
            mRegIx[i] = new IndexRegister();
        }
        mRegJ = new IndexRegister();
        mOverFlowToggle = new OverFlowToggle();
        mCompIndicator = new CompIndicator();
        mMemory = new Memory();

        mIOUnit[UNIT_NUMBER_LINE_PRINTER] = new LinePrinter(Thread.currentThread());
    }


    public void start() {

    }

    public void loadProgram(byte[] program) {

    }


    // Generic load operation.
    void load(Register reg, int addr, int left, int right) {
        checkFieldSpec(left, right);
        checkAddress(addr);

        reg.reset();
        Word mem = mMemory.get(addr);

        if (left == 0) {
            reg.setSign(mem.getSign());
            ++left;
        }

        for (int i = left; i <= right; ++i) {
            reg.setField(i, mMemory.get(addr).getField(i));
        }
        if (right > 0) {
            reg.shiftRight(Word.WORD_SIZE_IN_BYTES - 1 - right);
        }
    }

    void load_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fs = decodeFieldSpec(fieldSpec);
        load(reg, m, fs.first, fs.second);
    }

    Pair<Integer, Integer> decodeFieldSpec(int fieldSpec) {
        if (fieldSpec < 0) {
            throw new IllegalArgumentException("fieldSpec < 0.");
        }
        int left = fieldSpec / 8;
        int right = fieldSpec % 8;
        return new Pair<>(left, right);
    }

    // Generic negative load operation.
    void loadNegative(Register reg, int addr, int left, int right) {
        load(reg, addr, left, right);
        if (left == 0) {
            reg.setSign(-mMemory.get(addr).getSign());
        }
    }

    void loadNegative_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fs = decodeFieldSpec(fieldSpec);
        loadNegative(reg, m, fs.first, fs.second);
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
        //mMemory.get(addr).setSign(Word.PLUS);
    }

    // STZ
    // Same as STA, except that plus zero is stored.
    void storeZero(int addr, final int left, final int right) {
        checkFieldSpec(left, right);
        checkAddress(addr);

        Word word = mMemory.get(addr);

        for (int f = right; f >= left && f > 0; --f) {
            word.setField(f, 0);
        }
        if (left == 0) {
            word.setSign(Word.PLUS);
        }
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

    void store_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fSpec = decodeFieldSpec(fieldSpec);
        store(reg, m, fSpec.first, fSpec.second);
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
            throw new IndexOutOfBoundsException("addr: " + addr);
        }
    }




    // SLA
    void shiftLeftRegA(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number < 0.");
        }
        mRegA.shiftLeft(number);
    }

    // SRA
    void shiftRightRegA(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number < 0.");
        }
        mRegA.shiftRight(number);
    }

    // SLAX
    void shiftLeftRegAX() {
        mRegA.shiftLeft();
        mRegA.setField(5, mRegX.getField(1));
        mRegX.shiftLeft();
    }

    void shiftLeftRegAX(int shift) {
        for (int i = 0; i < shift; ++i) {
            shiftLeftRegAX();
        }
    }

    // SRAX
    void shiftRightRegAX() {
        mRegX.shiftRight();
        mRegX.setField(1, mRegA.getField(5));
        mRegA.shiftRight();
    }

    void shiftRightRegAX(int shift) {
        if (shift < 0) {
            throw new IllegalArgumentException("number < 0.");
        }
        for (int i = 0; i < shift; ++i) {
            shiftRightRegAX();
        }
    }

    // SLC
    void shiftLeftRegAXCircle() {
        int tmp = mRegA.getField(1);
        shiftLeftRegAX();
        mRegX.setField(5, tmp);
    }

    void shiftLeftRegAXCircle(int shift) {
        if (shift < 0) {
            throw new IllegalArgumentException("number < 0.");
        }
        for (int i = 0; i < shift; ++i) {
            shiftLeftRegAXCircle();
        }
    }

    // SRC
    void shiftRightRegAXCircle() {
        int tmp = mRegX.getField(5);
        shiftRightRegAX();
        mRegA.setField(1, tmp);
    }

    void shiftRightRegAXCircle(int shift) {
        if (shift < 0) {
            throw new IllegalArgumentException("number < 0.");
        }
        for (int i = 0; i < shift; ++i) {
            shiftRightRegAXCircle();
        }
    }

    // Arithmetic operators

    // ADD
    // If the result is zero, the sign of rA is unchanged.
    void add(int addr, int left, int right) {
        long quantity = mMemory.get(addr).getQuantity(left, right);
        add(quantity);
    }

    void add_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fSpec = decodeFieldSpec(fieldSpec);
        add_(reg, m, fSpec.first, fSpec.second);
    }

    void add(long quantity) {
        add(mRegA, quantity);
    }

    void add(Register reg, long quantity) {
        long rVal = reg.getQuantity();

        long sum = rVal + quantity;
        //long sumAbs = Math.abs(sum);

        // Word.MIN_VALUE == -WORD.MAX_VALUE
        if (sum > Word.MAX_VALUE) {
            mOverFlowToggle.setOverFlow(true);
            sum = sum - (Word.MAX_VALUE + 1);
        } else if (sum < Word.MIN_VALUE) {
            mOverFlowToggle.setOverFlow(true);
            sum = sum + (Word.MAX_VALUE + 1);
        }

        if (sum == 0) {
            reg.setQuantitySignUnchanged(0);
        } else {
            reg.setQuantity(sum < 0 ? Word.MINUS : Word.PLUS, Math.abs(sum));
        }
    }

    void subtract(int addr, int left, int right) {
        long quantity = mMemory.get(addr).getQuantity(left, right);
        add(-quantity);
    }

    void subtract_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fSpec = decodeFieldSpec(fieldSpec);
        subtract_(reg, m, fSpec.first, fSpec.second);
    }

    // The 10-byte product, V times rA, replaces register A and X.
    // The signs of rA and rX are both set to the algebraic sign of the product.
    void multiply(int addr, int left, int right) {

        long memVal = mMemory.get(addr).getQuantity(left, right);
        long rAVal = mRegA.getQuantity();
        long mul = memVal * rAVal;

        long mulAbs = Math.abs(mul);

        long low = mulAbs % Word.MAX_VALUE;
        long high = mulAbs / Word.MAX_VALUE;

        mRegA.setQuantitySignUnchanged(high);
        mRegX.setQuantitySignUnchanged(low);

        if (mMemory.get(addr).getSign() == mRegA.getSign()) {
            mRegA.setSignToPlus();
            mRegX.setSignToPlus();
        } else {
            mRegA.setSignToMinus();
            mRegX.setSignToMinus();
        }
    }

    void multiply_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fSpec = decodeFieldSpec(fieldSpec);
        multiply(m, fSpec.first, fSpec.second);
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

        long vAbs = Math.abs(mMemory.get(addr).getQuantity(left, right));
        int vSign;

        if (left == 0)
            vSign = mMemory.get(addr).getSign();
        else
            vSign = Word.PLUS;


        if (vAbs == 0 || rAAbs >= vAbs) { // |rA| >= |V|
            mOverFlowToggle.setOverFlow(true);
        } else {
            long rAXAbs = rAAbs * (Word.MAX_VALUE + 1) + rXAbs;

            long quotientAbs = rAXAbs / vAbs; // +-floor(|aAX/V|)

            long remainderAbs = rAXAbs % vAbs; // +-(|rAX| mod |V|)

            final int prevRegASign = mRegA.getSign();

            mRegA.setQuantitySignUnchanged(quotientAbs);
            mRegX.setQuantitySignUnchanged(remainderAbs);

            if (mRegA.getSign() != vSign) {
                mRegA.setSignToMinus();
            } else {
                mRegA.setSignToPlus();
            }

            mRegX.setSign(prevRegASign);
        }
    }

    void divide_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fSpec = decodeFieldSpec(fieldSpec);
        divide(m, fSpec.first, fSpec.second);
    }

    // Address transfer operators.

    // The quantity M is loaded into rA. If M = 0, the sign of the instruction is loaded.
    void enter(Register reg, int sign, int addr, int index) {
        if (sign != Word.PLUS && sign != Word.MINUS) {
            throw new IllegalArgumentException();
        }
        int m = calcM(sign, addr, index);

        int rSign = m == 0 ? sign : (m < 0 ? Word.MINUS : Word.PLUS);
        reg.setQuantity(rSign, Math.abs(m));
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
        int indexValue = index == 0 ? 0 : (int) mRegIx[index].getQuantity();
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
        long regValue = reg.getQuantity(left, right);
        long memValue = mMemory.get(addr).getQuantity(left, right);

        if (regValue == memValue) {
            mCompIndicator.setEqual();
        } else if (regValue < memValue) {
            mCompIndicator.setLess();
        } else if (regValue > memValue) {
            mCompIndicator.setGreater();
        }
    }

    void compare_(Register reg, int address, int index, int fieldSpec) {
        int m = calcM(address, index);
        Pair<Integer, Integer> fSpec = decodeFieldSpec(fieldSpec);
        compare_(reg, m, fSpec.first, fSpec.second);
    }

    void jump(int from, int to) {
        checkMemoryBound(from);
        checkMemoryBound(to);

        mRegJ.setQuantity(Word.PLUS, from);
        jumpSaveJ(to);
    }

    void jump_(int address, int index) {
        int m = calcM(address, index);
        jump(mPC, m);
    }

    void jumpSaveJ(int to) {
        checkMemoryBound(to);
        mPC = to;
    }

    void jumpSaveJ_(int address, int index) {
        int m = calcM(address, index);
        jumpSaveJ(m);
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

    void jumpOnOverflow_(int address, int index) {
        int m = calcM(address, index);
        jumpOnOverflow(mPC, m);
    }

    void jumpOnNoOverflow(int from, int to) {
        if (!mOverFlowToggle.isOverFlow()) {
            jump(from, to);
        } else {
            mOverFlowToggle.setOverFlow(false);
        }
    }

    void jumpOnNoOverflow_(int address, int index) {
        int m = calcM(address, index);
        jumpOnNoOverflow(mPC, m);
    }

    void jumpOnLess(int from, int to) {
        if (mCompIndicator.isLess()) {
            jump(from, to);
        }
    }

    void jumpOnLess_(int address, int index) {
        int m = calcM(address, index);
        jumpOnLess(mPC, m);
    }

    void jumpOnEqual(int from, int to) {
        if (mCompIndicator.isEqual()) {
            jump(from, to);
        }
    }

    void jumpOnEqual_(int address, int index) {
        int m = calcM(address, index);
        jumpOnEqual(mPC, m);
    }

    void jumpOnGreater(int from, int to) {
        if (mCompIndicator.isGreater()) {
            jump(from, to);
        }
    }

    void jumpOnGreater_(int address, int index) {
        int m = calcM(address, index);
        jumpOnGreater(mPC, m);
    }

    void jumpOnGreaterOrEqual(int from, int to) {
        if (mCompIndicator.isGreater() || mCompIndicator.isEqual()) {
            jump(from, to);
        }
    }

    void jumpOnGreaterOrEqual_(int address, int index) {
        int m = calcM(address, index);
        jumpOnGreaterOrEqual(mPC, m);
    }

    void jumpOnUnEqual(int from, int to) {
        if (mCompIndicator.isLess() || mCompIndicator.isGreater()) {
            jump(from, to);
        }
    }

    void jumpOnUnEqual_(int address, int index) {
        int m = calcM(address, index);
        jumpOnUnEqual(mPC, m);
    }

    void jumpOnLessOrEqual(int from, int to) {
        if (mCompIndicator.isLess() || mCompIndicator.isEqual()) {
            jump(from, to);
        }
    }

    void jumpOnLessOrEqual_(int address, int index) {
        int m = calcM(address, index);
        jumpOnLessOrEqual(mPC, m);
    }

    void jumpNegative(Register reg, int from, int to) {
        long quantity = reg.getQuantity();
        if (quantity < 0) {
            jump(from, to);
        }
    }

    void jumpNegative_(Register reg, int address, int index) {
        int m = calcM(address, index);
        jumpNegative(reg, mPC, m);
    }

    void jumpZero(Register reg, int from, int to) {
        long quantity = reg.getQuantity();
        if (quantity == 0) {
            jump(from, to);
        }
    }

    void jumpZero_(Register reg, int address, int index) {
        int m = calcM(address, index);
        jumpZero(reg, mPC, m);
    }

    void jumpPositive(Register reg, int from, int to) {
        long quantity = reg.getQuantity();
        if (quantity > 0) {
            jump(from, to);
        }
    }

    void jumpPositive_(Register reg, int address, int index) {
        int m = calcM(address, index);
        jumpPositive(reg, mPC, m);
    }

    void jumpNonnegative(Register reg, int from, int to) {
        long quantity = reg.getQuantity();
        if (!(quantity < 0)) {
            jump(from, to);
        }
    }

    void jumpNonnegative_(Register reg, int address, int index) {
        int m = calcM(address, index);
        jumpNonnegative(reg, mPC, m);
    }

    void jumpNonzero(Register reg, int from, int to) {
        long quantity = reg.getQuantity();
        if (quantity != 0) {
            jump(from, to);
        }
    }

    void jumpNonzero_(Register reg, int address, int index) {
        int m = calcM(address, index);
        jumpNonzero(reg, mPC, m);
    }

    void jumpNonpositive(Register reg, int from, int to) {
        long quantity = reg.getQuantity();
        if (!(quantity > 0)) {
            jump(from, to);
        }
    }

    void jumpNonpositive_(Register reg, int address, int index) {
        int m = calcM(address, index);
        jumpNonpositive(reg, mPC, m);
    }

    // MOVE
    // The number of words specified by F is moved, starting from location M to the location specified by the contents of index register 1.
    // The transfer occurs one word at a time, and rI1 is increased by the value of F at the end of the operation.
    // If F = 0, nothing happens.
    void move(int addr, int amount) {
        for (int _i = 0; _i < amount; ++_i) {
            int rI1 = (int) mRegIx[1].getQuantity();
            if (rI1 < 0) {
                throw new IllegalStateException("rI1 < 0.");
            }
            mMemory.get(addr).writeTo(mMemory.get(rI1));
            ++rI1;
            mRegIx[1].setQuantity(Word.PLUS, rI1);
            ++addr;
        }
    }

    void move_(int address, int index, int field) {
        int m = calcM(address, index);
        move(m, field);
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
     * Input or output with magnetic tape, disk, or drum units reads or writes full words (five bytes and a sign).
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
        /*mIOUnit[unit].waitUntilReady();

        // TODO: Async
        Word[] block = mIOUnit[unit].input(mMemory, addr);
        for (int i = 0; i < block.length; ++i) {
            mMemory.get(addr + i).writeTo(block[i]);
        }*/
    }

    void output(int addr, int unit) {

        IO outputDevice = mIOUnit[unit];
        if (outputDevice == null) {
            throw new IllegalArgumentException("Unsupported peripheral device.");
        }
        if (outputDevice.isInputDevice()) {
            throw new IllegalArgumentException("This device is for input.");
        }

        // Blocking
        outputDevice.waitUntilReady();

        outputDevice.output(mMemory, addr);

        /*// TODO: Async
        int blockSize = 24;//mIOUnit[unit].blockSize();

        Word[] block = new Word[blockSize];

        for (int i = 0; i < blockSize; ++i) {
            //block[i].writeTo(mMemory.get(addr + i));

            Word w = mMemory.get(addr + i);
            System.out.print(w.getField(1) % 10);
            System.out.print(w.getField(2) % 10);
            System.out.print(w.getField(3) % 10);
            System.out.print(w.getField(4) % 10);
            System.out.print(' ');
        }
        System.out.println();*/
        //mIOUnit[unit].output(block);
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

        if (numeric > Word.MAX_VALUE) {
            mOverFlowToggle.setOverFlow(true);
            numeric = numeric % (Word.MAX_VALUE + 1);
        }
        mRegA.setQuantitySignUnchanged(numeric);
    }

    // This operation is used to change numeric code into character code suitable for output to punched cards or tape or the line printer.
    // The value in rA is converted into a 10-byte decimal number that is put into registers A and X in character code.
    // The signs of rA and rX are unchanged. M is ignored.
    void convertToCharacters() {
        long rAAbs = Math.abs(mRegA.getQuantity());

        char[] digits = Long.toString(rAAbs, 10).toCharArray();

        int[] buf = new int[10];
        for (int i = 0; i < buf.length; ++i) {
            buf[i] = 0;
        }

        for (int i = digits.length - 1, f = 9; i >= 0; --i, --f) {
            buf[f] = Character.digit(digits[i], 10);
        }
        for (int i = 1; i <= 5; ++i) {
            mRegA.setField(i, 30 + buf[i - 1]);
            mRegX.setField(i, 30 + buf[4 + i]);
        }
    }

}
