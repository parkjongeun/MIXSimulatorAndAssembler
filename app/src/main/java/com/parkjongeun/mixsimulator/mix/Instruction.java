package com.parkjongeun.mixsimulator.mix;

import android.support.annotation.IntRange;

/**
 * Created by Parkjongeun on 2016. 9. 27..
 */
public class Instruction {

    final boolean mSign;
    @IntRange(from=0)
    final int mAA;
    final int mAddress;
    @IntRange(from=0, to=6)
    final int mIndex;
    final int mField;
    final OpCode mOpCode;

    final static int MINUS = -1;
    final static int PLUS = 1;


    public Instruction(boolean sign, int AA, int index, int field, String opCode) {
        if (AA < 0) {
            throw new IllegalArgumentException("AA < 0.");
        }
        if (index < 0 || index > 6) {
            throw new IllegalArgumentException("index < 0 || index > 6.");
        }
        if (field < 0) {
            throw new IllegalArgumentException("field < 0.");
        }
        mAA = AA;
        mSign = sign;
        mAddress = !sign ? AA : -AA;
        mIndex = index;
        mField = field;
        mOpCode = OpCode.valueOf(opCode);
    }

    public Instruction(boolean sign, int address, int index, String opCode) {
        this(sign, address, index, OpCode.valueOf(opCode).fieldSpec, opCode);
    }

    public Instruction(boolean sign, int address, String opCode) {
        this(sign, address, 0, OpCode.valueOf(opCode).fieldSpec, opCode);
    }

    public Instruction parse(String instruction) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public static Instruction fromWord(Word word) {
        int sign = word.getSign();
        int AA = (int) word.getQuantity(1, 2);
        int index = word.getField(3);
        int field = word.getField(4);
        int C = word.getField(5);
        try {
            OpCode opCode = OpCode.valueOf(C, field);
            return new Instruction(sign == Word.MINUS, AA, index, field, opCode.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid instruction format.");
        }
    }

    public int getSign() {
        return !mSign ? Word.PLUS : Word.MINUS;
    }

    public int getAA() {
        return mAA;
    }

    public int getI() {
        return mIndex;
    }

    public int getF() {
        return mField;
    }

    public int getC() {
        return mOpCode.code;
    }

    public int getAddress() {
        return mAddress;
    }

    void checkIntegrity() {
        if (mAA < 0) {
            throw new IllegalStateException("mAA < 0.");
        }
        if (mIndex < 0 || mIndex > 6) {
            throw new IllegalArgumentException("index < 0 || index > 6.");
        }
        if (mField < 0) {
            throw new IllegalArgumentException("field < 0.");
        }
    }

    public Word toWord() {
        checkIntegrity();

        Word w = new Word();
        w.reset();
        int sign = mSign ? Word.MINUS : Word.PLUS;
        int highAddr = mAA / Word.BYTE_SIZE;
        int lowAddr = mAA % Word.BYTE_SIZE;

        w.setSign(sign);
        w.setField(1, highAddr);
        w.setField(2, lowAddr);
        w.setField(3, mIndex);
        w.setField(4, mField);
        w.setField(5, mOpCode.code);

        return w;
    }
}
