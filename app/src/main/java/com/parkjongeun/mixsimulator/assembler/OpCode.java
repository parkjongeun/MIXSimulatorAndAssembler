package com.parkjongeun.mixsimulator.assembler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public enum OpCode {

    LDA(0x8),
    LDX(0xF),
    LD1(0x9),
    LD2(0xA),
    LD3(0xB),
    LD4(0xC),
    LD5(0xD),
    LD6(0xE),

    LDAN(0x10),
    LDXN(0x17),
    LD1N(0x11),
    LD2N(0x12),
    LD3N(0x13),
    LD4N(0x14),
    LD5N(0x15),
    LD6N(0x16),

    STA(0x18),
    STX(0x1F),
    ST1(0x19),
    ST2(0x1A),
    ST3(0x1B),
    ST4(0x1C),
    ST5(0x1D),
    ST6(0x1E),

    STJ(0x20, 2),
    STZ(0x21),

    ADD(0x1),
    SUB(0x2),
    MUL(0x3),
    DIV(0x4),

    ENTA(0x30, 2),
    ENTX(0x37, 2),
    ENT1(0x31, 2),
    ENT2(0x32, 2),
    ENT3(0x33, 2),
    ENT4(0x34, 2),
    ENT5(0x35, 2),
    ENT6(0x36, 2),
    ENNA(0x30, 3),
    ENNX(0x37, 3),
    ENN1(0x31, 3),
    ENN2(0x32, 3),
    ENN3(0x33, 3),
    ENN4(0x34, 3),
    ENN5(0x35, 3),
    ENN6(0x36, 3),

    INCA(0x30, 0),
    INCX(0x37, 0),
    INC1(0x31, 0),
    INC2(0x32, 0),
    INC3(0x33, 0),
    INC4(0x34, 0),
    INC5(0x35, 0),
    INC6(0x36, 0),
    DECA(0x30, 1),
    DECX(0x37, 1),
    DEC1(0x31, 1),
    DEC2(0x32, 1),
    DEC3(0x33, 1),
    DEC4(0x34, 1),
    DEC5(0x35, 1),
    DEC6(0x36, 1),

    CMPA(0x38),
    CMPX(0x3F),
    CMP1(0x39),
    CMP2(0x3A),
    CMP3(0x3B),
    CMP4(0x3C),
    CMP5(0x3D),
    CMP6(0x3E),

    JMP(0x27, 0),
    JSJ(0x27, 1),
    JOV(0x27, 2),
    JNOV(0x27, 3),
    JL(0x27, 4),
    JE(0x27, 5),
    JG(0x27, 6),
    JGE(0x27, 7),
    JNE(0x27, 8),
    JLE(0x27, 9),
    JAN(0x28, 0),
    JAZ(0x28, 1),
    JAP(0x28, 2),
    JANN(0x28, 3),
    JANZ(0x28, 4),
    JANP(0x28, 5),
    JXN(0x2F, 0),
    JXZ(0x2F, 1),
    JXP(0x2F, 2),
    JXNN(0x2F, 3),
    JXNZ(0x2F, 4),
    JXNP(0x2f, 5),
    J1N(0x29, 0),
    J2N(0x2A, 0),
    J3N(0x2B, 0),
    J4N(0x2C, 0),
    J5N(0x2D, 0),
    J6N(0x2E, 0),
    J1Z(0x29, 1),
    J2Z(0x2A, 1),
    J3Z(0x2B, 1),
    J4Z(0x2C, 1),
    J5Z(0x2D, 1),
    J6Z(0x2E, 1),
    J1P(0x29, 2),
    J2P(0x2A, 2),
    J3P(0x2B, 2),
    J4P(0x2C, 2),
    J5P(0x2D, 2),
    J6P(0x2E, 2),
    J1NN(0x29, 3),
    J2NN(0x2A, 3),
    J3NN(0x2B, 3),
    J4NN(0x2C, 3),
    J5NN(0x2D, 3),
    J6NN(0x2E, 3),
    J1NZ(0x29, 4),
    J2NZ(0x2A, 4),
    J3NZ(0x2B, 4),
    J4NZ(0x2C, 4),
    J5NZ(0x2D, 4),
    J6NZ(0x2E, 4),
    J1NP(0x29, 5),
    J2NP(0x2A, 5),
    J3NP(0x2B, 5),
    J4NP(0x2C, 5),
    J5NP(0x2D, 5),
    J6NP(0x2E, 5),

    SLA(0x6, 0),
    SRA(0x6, 1),
    SLAX(0x6, 2),
    SRAX(0x6, 3),
    SLC(0x6, 4),
    SRC(0x6, 5),

    MOVE(0x7, 1),
    NOP(0x0, 0),
    HLT(0x5, 2),

    IN(0x24, 0),
    OUT(0x25, 0),
    IOC(0x23, 0),
    JRED(0x26, 0),
    JBUS(0x22, 0),
    NUM(0x5, 0),
    CHAR(0x5, 1);


    public int code;
    public int fieldSpec;

    OpCode(int code) {
        this(code, 5);
    }

    OpCode(int code, int fieldSpec) {
        this.code = code;
        this.fieldSpec = fieldSpec;
    }

    public static OpCode valueOf(int C, int F) {
        OpCode[] vs = values();
        List<OpCode> candidates = new ArrayList<>();
        for (int i = 0; i < vs.length; ++i) {
            if (vs[i].code == C) {
                candidates.add(vs[i]);
            }
        }
        if (candidates.size() == 0) {
            throw new IllegalArgumentException("C: " + C + " F: " + F);
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        } else {
            for (int i = 0; i < candidates.size(); ++i) {
                if (candidates.get(i).fieldSpec == F) {
                    return candidates.get(i);
                }
            }
            throw new IllegalArgumentException("C: " + C + " F: " + F);
        }
    }
}
