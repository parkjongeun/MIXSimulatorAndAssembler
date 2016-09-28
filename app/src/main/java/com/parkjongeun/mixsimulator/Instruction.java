package com.parkjongeun.mixsimulator;

/**
 * Created by Parkjongeun on 2016. 9. 27..
 */
public class Instruction {

    boolean mSign;
    int mAddress;
    int mIndex;
    int mField;

    OpCode mOpCode;

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

    /*public Instruction parse(String instruction) {

        return new Instruction();
    }*/
}
