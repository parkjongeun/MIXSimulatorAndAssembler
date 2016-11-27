package com.parkjongeun.mixsimulator.mix;

import android.support.annotation.IntRange;

/**
 * Created by Parkjongeun on 2016. 9. 27..
 */
public class Instruction {

    final boolean mSign;
    @IntRange(from=0)
    final int mAddress;
    @IntRange(from=0, to=6)
    final int mIndex;
    final int mField;
    final OpCode mOpCode;

    final static int MINUS = -1;
    final static int PLUS = 1;


    public Instruction(boolean sign, int address, int index, int field, String opCode) {
        if (address < 0) {
            throw new IllegalArgumentException("address < 0.");
        }
        if (index < 0 || index > 6) {
            throw new IllegalArgumentException("index < 0 || index > 6.");
        }
        if (field < 0) {
            throw new IllegalArgumentException("field < 0.");
        }
        mSign = sign;
        mAddress = address;
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
        int address = (int) word.getQuantity(1, 2);
        int index = word.getField(3);
        int field = word.getField(4);
        int opCode_ = word.getField(5);
        OpCode opCode = OpCode.valueOf(opCode_, field);
        try {
            Instruction instruction = new Instruction(sign == Word.MINUS, address, index, field, opCode.name());
            return instruction;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid instruction format.");
        }
    }

    void checkIntegrity() {
        if (mAddress < 0) {
            throw new IllegalStateException("mAddress < 0.");
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
        int highAddr = mAddress / Word.BYTE_SIZE;
        int lowAddr = mAddress % Word.BYTE_SIZE;

        w.setSign(sign);
        w.setField(1, highAddr);
        w.setField(2, lowAddr);
        w.setField(3, mIndex);
        w.setField(4, mField);
        w.setField(5, mOpCode.code);

        return w;
    }
}
