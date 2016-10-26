package com.parkjongeun.mixsimulator;

import android.content.OperationApplicationException;

import com.parkjongeun.mixsimulator.util.Pair;


/**
 * Created by Parkjongeun on 2016. 9. 27..
 */
public class Executor {

    Mix mMix;

    public Executor(Mix mix) {
        mMix = mix;
    }

    void start(int address) {
        mMix.mPC = address;

        int cycle = 0;

        while (true) {
            Word w = mMix.mMemory.read(mMix.mPC);
            ++mMix.mPC;
            // NOP
            if (w.getField(5) == 0) {
                break;
            }
            Instruction instruction = Instruction.fromWord(w);
            execute(instruction);

            //System.out.println(++cycle);
        }
    }

    void execute(Instruction[] pgm) {
        for (int i = 0; i < pgm.length; ++i) {
            execute(pgm[i]);
        }
    }

    void execute(Instruction instruction) {

        OpCode opCode = instruction.mOpCode;
        boolean sign_ = instruction.mSign;
        int sign = sign_ ? Word.MINUS : Word.PLUS;
        int address = (sign_ ? -1 : 1) * instruction.mAddress;
        int index = instruction.mIndex;
        int field = instruction.mField;
        Pair<Integer, Integer> fSpec = decodeFieldSpec(field);
        int L = fSpec.first;
        int R = fSpec.second;
        Pair<Integer, Integer> M_ = calculateM(sign, address, index);
        //int MSign = M_.first;
        int M = M_.second;

        switch(opCode) {
            case LDA: mMix.load(mMix.mRegA, M, L, R); break;
            case LDX: mMix.load(mMix.mRegX, M, L, R); break;
            case LD1: mMix.load(mMix.mRegIx[1], M, L, R); break;
            case LD2: mMix.load(mMix.mRegIx[2], M, L, R); break;
            case LD3: mMix.load(mMix.mRegIx[3], M, L, R); break;
            case LD4: mMix.load(mMix.mRegIx[4], M, L, R); break;
            case LD5: mMix.load(mMix.mRegIx[5], M, L, R); break;
            case LD6: mMix.load(mMix.mRegIx[6], M, L, R); break;

            case LDAN: mMix.loadNegative(mMix.mRegA, M, L, R); break;
            case LDXN: mMix.loadNegative(mMix.mRegX, M, L, R); break;
            case LD1N: mMix.loadNegative(mMix.mRegIx[1], M, L, R); break;
            case LD2N: mMix.loadNegative(mMix.mRegIx[2], M, L, R); break;
            case LD3N: mMix.loadNegative(mMix.mRegIx[3], M, L, R); break;
            case LD4N: mMix.loadNegative(mMix.mRegIx[4], M, L, R); break;
            case LD5N: mMix.loadNegative(mMix.mRegIx[5], M, L, R); break;
            case LD6N: mMix.loadNegative(mMix.mRegIx[6], M, L, R); break;

            case STA: mMix.store(mMix.mRegA, M, L, R); break;
            case STX: mMix.store(mMix.mRegX, M, L, R); break;
            case ST1: mMix.store(mMix.mRegIx[1], M, L, R); break;
            case ST2: mMix.store(mMix.mRegIx[2], M, L, R); break;
            case ST3: mMix.store(mMix.mRegIx[3], M, L, R); break;
            case ST4: mMix.store(mMix.mRegIx[4], M, L, R); break;
            case ST5: mMix.store(mMix.mRegIx[5], M, L, R); break;
            case ST6: mMix.store(mMix.mRegIx[6], M, L, R); break;

            case STJ: mMix.store(mMix.mRegJ, M, L, R); break;
            case STZ: mMix.storeZero(M, L, R); break;

            case ADD: mMix.add(M, L, R); break;
            case SUB: mMix.subtract(M, L, R); break;
            case MUL: mMix.multiply(M, L, R); break;
            case DIV: mMix.divide(M, L, R); break;

            case ENTA: mMix.enter(mMix.mRegA, sign, address, index); break;
            case ENTX: mMix.enter(mMix.mRegX, sign, address, index); break;
            case ENT1: mMix.enter(mMix.mRegIx[1], sign, address, index); break;
            case ENT2: mMix.enter(mMix.mRegIx[2], sign, address, index); break;
            case ENT3: mMix.enter(mMix.mRegIx[3], sign, address, index); break;
            case ENT4: mMix.enter(mMix.mRegIx[4], sign, address, index); break;
            case ENT5: mMix.enter(mMix.mRegIx[5], sign, address, index); break;
            case ENT6: mMix.enter(mMix.mRegIx[6], sign, address, index); break;
            case ENNA: mMix.enterNegative(mMix.mRegA, sign, address, index); break;
            case ENNX: mMix.enterNegative(mMix.mRegX, sign, address, index); break;
            case ENN1: mMix.enterNegative(mMix.mRegIx[1], sign, address, index); break;
            case ENN2: mMix.enterNegative(mMix.mRegIx[2], sign, address, index); break;
            case ENN3: mMix.enterNegative(mMix.mRegIx[3], sign, address, index); break;
            case ENN4: mMix.enterNegative(mMix.mRegIx[4], sign, address, index); break;
            case ENN5: mMix.enterNegative(mMix.mRegIx[5], sign, address, index); break;
            case ENN6: mMix.enterNegative(mMix.mRegIx[6], sign, address, index); break;

            case INCA: mMix.increase(mMix.mRegA, address, index); break;
            case INCX: mMix.increase(mMix.mRegX, address, index); break;
            case INC1: mMix.increase(mMix.mRegIx[1], address, index); break;
            case INC2: mMix.increase(mMix.mRegIx[2], address, index); break;
            case INC3: mMix.increase(mMix.mRegIx[3], address, index); break;
            case INC4: mMix.increase(mMix.mRegIx[4], address, index); break;
            case INC5: mMix.increase(mMix.mRegIx[5], address, index); break;
            case INC6: mMix.increase(mMix.mRegIx[6], address, index); break;
            case DECA: mMix.decrease(mMix.mRegA, address, index); break;
            case DECX: mMix.decrease(mMix.mRegX, address, index); break;
            case DEC1: mMix.decrease(mMix.mRegIx[1], address, index); break;
            case DEC2: mMix.decrease(mMix.mRegIx[2], address, index); break;
            case DEC3: mMix.decrease(mMix.mRegIx[3], address, index); break;
            case DEC4: mMix.decrease(mMix.mRegIx[4], address, index); break;
            case DEC5: mMix.decrease(mMix.mRegIx[5], address, index); break;
            case DEC6: mMix.decrease(mMix.mRegIx[6], address, index); break;

            case CMPA: mMix.compare(mMix.mRegA, M, L, R); break;
            case CMPX: mMix.compare(mMix.mRegX, M, L, R); break;
            case CMP1: mMix.compare(mMix.mRegIx[1], M, L, R); break;
            case CMP2: mMix.compare(mMix.mRegIx[2], M, L, R); break;
            case CMP3: mMix.compare(mMix.mRegIx[3], M, L, R); break;
            case CMP4: mMix.compare(mMix.mRegIx[4], M, L, R); break;
            case CMP5: mMix.compare(mMix.mRegIx[5], M, L, R); break;
            case CMP6: mMix.compare(mMix.mRegIx[6], M, L, R); break;

            case JMP: mMix.jump_(address, index); break;
            case JSJ: mMix.jumpSaveJ_(address, index); break;
            case JOV: mMix.jumpOnOverflow_(address, index); break;
            case JNOV: mMix.jumpOnNoOverflow_(address, index); break;
            case JL: mMix.jumpOnLess_(address, index); break;
            case JE: mMix.jumpOnEqual_(address, index); break;
            case JG: mMix.jumpOnGreater_(address, index); break;
            case JGE: mMix.jumpOnGreaterOrEqual_(address, index); break;
            case JNE: mMix.jumpOnUnEqual_(address, index); break;
            case JLE: mMix.jumpOnLessOrEqual_(address, index); break;

            case JAN: mMix.jumpNegative_(mMix.mRegA, address, index); break;
            case JAZ: mMix.jumpZero_(mMix.mRegA, address, index); break;
            case JAP: mMix.jumpPositive_(mMix.mRegA, address, index); break;
            case JANN: mMix.jumpNonnegative_(mMix.mRegA, address, index); break;
            case JANZ: mMix.jumpNonzero_(mMix.mRegA, address, index); break;
            case JANP: mMix.jumpNonpositive_(mMix.mRegA, address, index); break;

            case JXN: mMix.jumpNegative_(mMix.mRegX, address, index); break;
            case JXZ: mMix.jumpZero_(mMix.mRegX, address, index); break;
            case JXP: mMix.jumpPositive_(mMix.mRegX, address, index); break;
            case JXNN: mMix.jumpNonnegative_(mMix.mRegX, address, index); break;
            case JXNZ: mMix.jumpNonzero_(mMix.mRegX, address, index); break;
            case JXNP: mMix.jumpNonpositive_(mMix.mRegX, address, index); break;


            case J1N: mMix.jumpNegative_(mMix.mRegIx[1], address, index); break;
            case J2N: mMix.jumpNegative_(mMix.mRegIx[2], address, index); break;
            case J3N: mMix.jumpNegative_(mMix.mRegIx[3], address, index); break;
            case J4N: mMix.jumpNegative_(mMix.mRegIx[4], address, index); break;
            case J5N: mMix.jumpNegative_(mMix.mRegIx[5], address, index); break;
            case J6N: mMix.jumpNegative_(mMix.mRegIx[6], address, index); break;
            case J1Z: mMix.jumpZero_(mMix.mRegIx[1], address, index); break;
            case J2Z: mMix.jumpZero_(mMix.mRegIx[2], address, index); break;
            case J3Z: mMix.jumpZero_(mMix.mRegIx[3], address, index); break;
            case J4Z: mMix.jumpZero_(mMix.mRegIx[4], address, index); break;
            case J5Z: mMix.jumpZero_(mMix.mRegIx[5], address, index); break;
            case J6Z: mMix.jumpZero_(mMix.mRegIx[6], address, index); break;
            case J1P: mMix.jumpPositive_(mMix.mRegIx[1], address, index); break;
            case J2P: mMix.jumpPositive_(mMix.mRegIx[2], address, index); break;
            case J3P: mMix.jumpPositive_(mMix.mRegIx[3], address, index); break;
            case J4P: mMix.jumpPositive_(mMix.mRegIx[4], address, index); break;
            case J5P: mMix.jumpPositive_(mMix.mRegIx[5], address, index); break;
            case J6P: mMix.jumpPositive_(mMix.mRegIx[6], address, index); break;
            case J1NN: mMix.jumpNonnegative_(mMix.mRegIx[1], address, index); break;
            case J2NN: mMix.jumpNonnegative_(mMix.mRegIx[2], address, index); break;
            case J3NN: mMix.jumpNonnegative_(mMix.mRegIx[3], address, index); break;
            case J4NN: mMix.jumpNonnegative_(mMix.mRegIx[4], address, index); break;
            case J5NN: mMix.jumpNonnegative_(mMix.mRegIx[5], address, index); break;
            case J6NN: mMix.jumpNonnegative_(mMix.mRegIx[6], address, index); break;
            case J1NZ: mMix.jumpNonzero_(mMix.mRegIx[1], address, index); break;
            case J2NZ: mMix.jumpNonzero_(mMix.mRegIx[2], address, index); break;
            case J3NZ: mMix.jumpNonzero_(mMix.mRegIx[3], address, index); break;
            case J4NZ: mMix.jumpNonzero_(mMix.mRegIx[4], address, index); break;
            case J5NZ: mMix.jumpNonzero_(mMix.mRegIx[5], address, index); break;
            case J6NZ: mMix.jumpNonzero_(mMix.mRegIx[6], address, index); break;
            case J1NP: mMix.jumpNonpositive_(mMix.mRegIx[1], address, index); break;
            case J2NP: mMix.jumpNonpositive_(mMix.mRegIx[2], address, index); break;
            case J3NP: mMix.jumpNonpositive_(mMix.mRegIx[3], address, index); break;
            case J4NP: mMix.jumpNonpositive_(mMix.mRegIx[4], address, index); break;
            case J5NP: mMix.jumpNonpositive_(mMix.mRegIx[5], address, index); break;
            case J6NP: mMix.jumpNonpositive_(mMix.mRegIx[6], address, index); break;

            case SLA: mMix.shiftLeftRegA(M); break;
            case SRA: mMix.shiftRightRegA(M); break;
            case SLAX: mMix.shiftLeftRegAX(M); break;
            case SRAX: mMix.shiftRightRegAX(M); break;
            case SLC: mMix.shiftLeftRegAXCircle(M); break;
            case SRC: mMix.shiftRightRegAXCircle(M); break;

            case MOVE: mMix.move(M, field); break;
            case NOP: break;
            case HLT: break;

            case IN: break;
            case OUT: mMix.output(M, field); break;
            case IOC: break;
            case JRED: break;
            case JBUS: break;
            case NUM: mMix.convertToNumeric(); break;
            case CHAR: mMix.convertToCharacters(); break;

            default:
                throw new IllegalStateException("No match op-code.");
        }

    }

    Pair<Integer, Integer> decodeFieldSpec(int fieldSpec) {
        if (fieldSpec < 0) {
            throw new IllegalArgumentException("fieldSpec < 0.");
        }
        int left = fieldSpec / 8;
        int right = fieldSpec % 8;
        return new Pair<Integer, Integer>(left, right);
    }

    int calcM(int addr, int index) {
        int indexValue = index == 0 ? 0 : mMix.mRegIx[index].getQuantity();
        int m = addr + indexValue;
        return m;
    }

    Pair<Integer, Integer> calculateM(int sign, int address, int index) {
        int indexValue = index == 0 ? 0 : mMix.mRegIx[index].getQuantity();
        int m = address + indexValue;
        if (m == 0) {
            return new Pair<>(sign, m);
        } else {
            return new Pair<>(m < 0 ? Word.MINUS : Word.PLUS, m);
        }
    }

//    int calcM(int sign, int addr, int index) {
//        return calcM((sign == Word.MINUS ? -1 : 1) * Math.abs(addr), index);
//    }


}
