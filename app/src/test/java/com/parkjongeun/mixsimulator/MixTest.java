package com.parkjongeun.mixsimulator;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Parkjongeun on 2016. 9. 26..
 */
public class MixTest {



    public MixTest() {
    }

    @Test
    public void load() {
        Mix mix = new Mix();
        mix.mMemory.get(1000).setQuantity(Word.MINUS, 564839);
        mix.mMemory.get(1001).setQuantity(Word.PLUS, 564839);
        mix.mMemory.get(0).setQuantity(Word.MINUS, 564839);
        mix.mMemory.get(3999).setQuantity(Word.PLUS, 564839);

        mix.load(mix.mRegA, 1000, 0, 5);
        assertEquals(mix.mRegA.getQuantity(), -564839);

        mix.load(mix.mRegA, 1001, 0, 5);
        assertEquals(mix.mRegA.getQuantity(), 564839);

        mix.load(mix.mRegA, 0, 0, 5);
        assertEquals(mix.mRegA.getQuantity(), -564839);

        mix.load(mix.mRegA, 3999, 0, 5);
        assertEquals(mix.mRegA.getQuantity(), 564839);
        //mix.mMemory.get(4000).setQuantity(Word.MINUS, 432);
    }

    @Test
    public void add() {
        Mix mix = new Mix();
        mix.mMemory.get(0).setQuantity(Word.MINUS, 564839);
        mix.mMemory.get(2).setQuantity(Word.MINUS, 564839);

        mix.load(mix.mRegA, 0, 0, 5);
        mix.add(2, 0, 5);
        assertEquals(mix.mRegA.getQuantity(), -564839 - 564839);
        assertEquals(mix.mOverFlowToggle.isOverFlow(), false);

        mix.mMemory.get(3).setQuantity(Word.MINUS, Word.MIN_VALUE);

        mix.add(3, 0, 5);
        assertEquals(mix.mRegA.getQuantity(), (-564839 - 564839 + Word.MIN_VALUE) % -Word.MAX_VALUE);
        assertEquals(mix.mOverFlowToggle.isOverFlow(), true);
    }

    @Test
    public void divide() {
        Mix mix = new Mix();
        mix.mMemory.get(0).setQuantity(Word.PLUS, 564839);
        mix.mMemory.get(1).setQuantity(Word.MINUS, 564839);

        mix.load(mix.mRegX, 0, 0, 5);
        mix.divide(1, 0, 5);

        assertEquals(-1, mix.mRegA.getQuantity());
        assertEquals(0, mix.mRegX.getQuantity());
        assertEquals(Word.PLUS, mix.mRegX.getSign());
        assertEquals(false, mix.mOverFlowToggle.isOverFlow());

        mix.mMemory.get(2).setQuantity(Word.PLUS, 564839);
        mix.mMemory.get(3).setQuantity(Word.MINUS, 564839);
        mix.mMemory.get(4).setQuantity(Word.MINUS, 564840);
        mix.load(mix.mRegA, 2, 0, 5);
        mix.load(mix.mRegX, 3, 0, 5);

        mix.divide(4, 0, 5);
        assertEquals((564839L * (Word.MAX_VALUE + 1) + 564839) / -564840, mix.mRegA.getQuantity());
        assertEquals((564839L * (Word.MAX_VALUE + 1) + 564839) % -564840, mix.mRegX.getQuantity());
        assertEquals(false, mix.mOverFlowToggle.isOverFlow());

        mix.mMemory.get(5).setQuantity(Word.PLUS, 564839);
        mix.mMemory.get(6).setQuantity(Word.MINUS, 564839);
        mix.mMemory.get(7).setQuantity(Word.MINUS, 564838);
        mix.load(mix.mRegA, 5, 0, 5);
        mix.load(mix.mRegX, 6, 0, 5);
        mix.divide(7, 0, 5);
        assertEquals(true, mix.mOverFlowToggle.isOverFlow());
    }

    @Test
    public void multiply() {

    }

    @Test
    public void enter() {
        Mix mix = new Mix();

        mix.enter(mix.mRegA, Word.MINUS, 84394);
        assertEquals(-84394, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());

        mix.enter(mix.mRegA, Word.MINUS, 0);
        assertEquals(-0, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.MINUS, 0);
        mix.enter(mix.mRegA, Word.PLUS, 0, 2);
        assertEquals(0, mix.mRegA.getQuantity());
        assertEquals(Word.PLUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.PLUS, 0);
        mix.enter(mix.mRegA, Word.MINUS, 0, 2);
        assertEquals(0, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.PLUS, 0);
        mix.enter(mix.mRegA, Word.PLUS, 0, 2);
        assertEquals(0, mix.mRegA.getQuantity());
        assertEquals(Word.PLUS, mix.mRegA.getSign());

        final int TWO_BYTE_MAX_VALUE = Word.BYTE_SIZE * Word.BYTE_SIZE - 1;
        mix.enter(mix.mRegIx[2], Word.MINUS, TWO_BYTE_MAX_VALUE);
        mix.enter(mix.mRegA, Word.MINUS, 0, 2);
        assertEquals(-TWO_BYTE_MAX_VALUE, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());

        /*mix.enter(mix.mRegIx[2], Word.MINUS, Word.MAX_VALUE);
        mix.enter(mix.mRegA, Word.MINUS, 1, 2);
        assertEquals(-Word.MAX_VALUE, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());*/

        mix.enter(mix.mRegIx[2], Word.PLUS, TWO_BYTE_MAX_VALUE);
        mix.enter(mix.mRegA, Word.MINUS, 0, 2);
        assertEquals(TWO_BYTE_MAX_VALUE, mix.mRegA.getQuantity());
        assertEquals(Word.PLUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.MINUS, 0);
        mix.enterNegative(mix.mRegA, Word.PLUS, 0, 2);
        assertEquals(0, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.PLUS, 0);
        mix.enterNegative(mix.mRegA, Word.MINUS, 0, 2);
        assertEquals(0, mix.mRegA.getQuantity());
        assertEquals(Word.PLUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.PLUS, 658);
        mix.enter(mix.mRegA, Word.MINUS, 333, 2);
        assertEquals(-333 + 658, mix.mRegA.getQuantity());
        assertEquals(Word.PLUS, mix.mRegA.getSign());

        mix.enter(mix.mRegIx[2], Word.PLUS, 658);
        mix.enterNegative(mix.mRegA, Word.MINUS, 333, 2);
        assertEquals(-(-333 + 658), mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());
    }

    @Test
    public void increase_decrease() {
        Mix mix = new Mix();

        mix.enter(mix.mRegA, Word.MINUS, 0);
        mix.increase(mix.mRegA, 1, 0);
        assertEquals(1, mix.mRegA.getQuantity());
        assertEquals(Word.PLUS, mix.mRegA.getSign());

        mix.enter(mix.mRegA, Word.PLUS, 0);
        mix.decrease(mix.mRegA, 1, 0);
        assertEquals(-1, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());

        mix.enter(mix.mRegA, Word.MINUS, 1);
        mix.decrease(mix.mRegA, Word.MAX_VALUE, 0);
        assertEquals((-1 - Word.MAX_VALUE) % Word.MAX_VALUE, mix.mRegA.getQuantity());
        assertEquals(Word.MINUS, mix.mRegA.getSign());
        assertEquals(true, mix.mOverFlowToggle.isOverFlow());
    }

    @Test
    public void compare() {
        Mix mix = new Mix();

        mix.enter(mix.mRegA, Word.MINUS, 7823);
        mix.mMemory.get(3999).setQuantity(Word.MINUS, 7823);
        mix.compare(mix.mRegA, 3999, 0, 5);
        assertEquals(true, mix.mCompIndicator.isEqual());

        mix.enter(mix.mRegA, Word.MINUS, 7823);
        mix.mMemory.get(3999).setQuantity(Word.PLUS, 7823);
        mix.compare(mix.mRegA, 3999, 3, 5);
        assertEquals(true, mix.mCompIndicator.isEqual());

        mix.compare(mix.mRegA, 3999, 5, 5);
        assertEquals(true, mix.mCompIndicator.isEqual());
        mix.compare(mix.mRegA, 3999, 4, 4);
        assertEquals(true, mix.mCompIndicator.isEqual());
        mix.compare(mix.mRegA, 3999, 3, 3);
        assertEquals(true, mix.mCompIndicator.isEqual());
        mix.compare(mix.mRegA, 3999, 2, 2);
        assertEquals(true, mix.mCompIndicator.isEqual());
        mix.compare(mix.mRegA, 3999, 1, 1);
        assertEquals(true, mix.mCompIndicator.isEqual());

        mix.compare(mix.mRegA, 3999, 1, 3);
        assertEquals(true, mix.mCompIndicator.isEqual());

        mix.compare(mix.mRegA, 3999, 2, 4);
        assertEquals(true, mix.mCompIndicator.isEqual());

        mix.compare(mix.mRegA, 3999, 2, 5);
        assertEquals(true, mix.mCompIndicator.isEqual());

        mix.compare(mix.mRegA, 3999, 0, 4);
        assertEquals(true, mix.mCompIndicator.isLess());

        mix.enter(mix.mRegA, Word.PLUS, 7823);
        mix.mMemory.get(3999).setQuantity(Word.PLUS, 7833);
        mix.compare(mix.mRegA, 3999, 3, 5);
        assertEquals(true, mix.mCompIndicator.isLess());

        mix.enter(mix.mRegIx[1], Word.PLUS, 723);
        mix.mMemory.get(3999).setQuantity(Word.MINUS, 723);
        mix.compare(mix.mRegIx[1], 3999, 0, 5);
        assertNotEquals(true, mix.mCompIndicator.isEqual());

        mix.compare(mix.mRegIx[1], 3999, 1, 5);
        assertEquals(true, mix.mCompIndicator.isEqual());
    }

    @Test
    public void jump() {
        Mix mix = new Mix();

        mix.jump(0, 3999);
        assertEquals(3999, mix.mPC);
        assertEquals(1, mix.mRegJ.getQuantity());

        mix.jump(3999, mix.mRegJ.getQuantity());
        assertEquals(1, mix.mPC);
        assertEquals(4000, mix.mRegJ.getQuantity());

        mix.jumpSaveJ(2000);
        assertEquals(2000, mix.mPC);
        assertEquals(4000, mix.mRegJ.getQuantity());

        int pc = mix.mPC;
        int rJ = mix.mRegJ.getQuantity();
        mix.jumpOnOverflow(1000, 3000);
        assertEquals(pc, mix.mPC);
        assertEquals(rJ, mix.mRegJ.getQuantity());

        mix.mOverFlowToggle.setOverFlow(true);
        assertEquals(true, mix.mOverFlowToggle.isOverFlow());
        mix.jumpOnOverflow(1000, 3000);
        assertEquals(3000, mix.mPC);
        assertEquals(1001, mix.mRegJ.getQuantity());
        assertEquals(false, mix.mOverFlowToggle.isOverFlow());


        mix.mOverFlowToggle.setOverFlow(true);
        assertEquals(true, mix.mOverFlowToggle.isOverFlow());
        pc = mix.mPC;
        rJ = mix.mRegJ.getQuantity();
        mix.jumpOnNoOverflow(1000, 3000);
        assertEquals(pc, mix.mPC);
        assertEquals(rJ, mix.mRegJ.getQuantity());
        assertEquals(false, mix.mOverFlowToggle.isOverFlow());

        mix.jumpOnNoOverflow(1000, 3000);
        assertEquals(3000, mix.mPC);
        assertEquals(1001, mix.mRegJ.getQuantity());
        assertEquals(false, mix.mOverFlowToggle.isOverFlow());

        mix.mCompIndicator.setLess();
        mix.jumpOnLess(1532, 543);
        assertEquals(543, mix.mPC);
        assertEquals(1533, mix.mRegJ.getQuantity());
        assertEquals(true, mix.mCompIndicator.isLess());

        mix.enter(mix.mRegX, Word.MINUS, 1);
        mix.jumpNegative(mix.mRegX, 389, 35);
        assertEquals(35, mix.mPC);
        assertEquals(390, mix.mRegJ.getQuantity());

        mix.enter(mix.mRegIx[1], Word.MINUS, 1);
        mix.jumpNegative(mix.mRegIx[1], 1389, 135);
        assertEquals(135, mix.mPC);
        assertEquals(1390, mix.mRegJ.getQuantity());

        mix.enter(mix.mRegIx[1], Word.MINUS, 0);
        mix.jumpNonpositive(mix.mRegIx[1], 2389, 235);
        assertEquals(235, mix.mPC);
        assertEquals(2390, mix.mRegJ.getQuantity());

        mix.enter(mix.mRegIx[1], Word.MINUS, -1);
        mix.jumpNonnegative(mix.mRegIx[1], 3389, 335);
        pc = mix.mPC;
        rJ = mix.mRegJ.getQuantity();
        assertEquals(pc, mix.mPC);
        assertEquals(rJ, mix.mRegJ.getQuantity());
    }

    @Test
    public void shift() {
        Mix mix = new Mix();

        for (int i = 1; i <= 5; ++i) {
            mix.mMemory.get(0).setField(i, i);
            mix.mMemory.get(1).setField(i, 5 + i);
        }
        mix.mMemory.get(0).setSign(Word.PLUS);
        mix.mMemory.get(1).setSign(Word.MINUS);

        mix.load(mix.mRegA, 0, 0, 5);
        mix.load(mix.mRegX, 1, 0, 5);

        mix.shiftRightRegAX();
        assertEquals(0, mix.mRegA.getField(1));
        assertEquals(9, mix.mRegX.getField(5));

        mix.shiftLeftRegA(2);
        assertEquals(2, mix.mRegA.getField(1));
        assertEquals(0, mix.mRegA.getField(4));
        assertEquals(9, mix.mRegX.getField(5));

        mix.shiftRightRegAXCircle();
        mix.shiftRightRegAXCircle();
        mix.shiftRightRegAXCircle();
        mix.shiftRightRegAXCircle();
        assertEquals(6, mix.mRegA.getField(1));
        assertEquals(9, mix.mRegA.getField(4));
        assertEquals(3, mix.mRegX.getField(1));
        assertEquals(5, mix.mRegX.getField(5));

        mix.shiftRightRegA(2);
        assertEquals(0, mix.mRegA.getField(1));
        assertEquals(7, mix.mRegA.getField(4));
        assertEquals(3, mix.mRegX.getField(1));
        assertEquals(5, mix.mRegX.getField(5));

        for (int i = 0; i < 501; ++i) {
            mix.shiftLeftRegAXCircle();
        }
        assertEquals(0, mix.mRegA.getField(1));
        assertEquals(8, mix.mRegA.getField(4));
        assertEquals(4, mix.mRegX.getField(1));
        assertEquals(0, mix.mRegX.getField(5));
    }

    @Test
    public void move() {
        Mix mix = new Mix();

        mix.mMemory.get(11).setQuantity(Word.MINUS, 378234);
        mix.mMemory.get(12).setQuantity(Word.MINUS, 838263);
        mix.enter(mix.mRegIx[1], Word.PLUS, 1111);
        mix.move(11, 2);
        assertEquals(-378234, mix.mMemory.get(1111).getQuantity());
        assertEquals(-838263, mix.mMemory.get(1112).getQuantity());
        assertEquals(1111 + 2, mix.mRegIx[1].getQuantity());
    }

    @Test
    public void io() {

    }

    @Test
    public void conversion() {
        Mix mix = new Mix();

        mix.mMemory.get(0).setSign(Word.MINUS);
        mix.mMemory.get(0).setField(1, 0);
        mix.mMemory.get(0).setField(2, 0);
        mix.mMemory.get(0).setField(3, 31);
        mix.mMemory.get(0).setField(4, 32);
        mix.mMemory.get(0).setField(5, 39);

        mix.mMemory.get(1).setSign(Word.PLUS);
        mix.mMemory.get(1).setField(1, 37);
        mix.mMemory.get(1).setField(2, 57);
        mix.mMemory.get(1).setField(3, 47);
        mix.mMemory.get(1).setField(4, 30);
        mix.mMemory.get(1).setField(5, 30);

        mix.load(mix.mRegA, 0, 0, 5);
        mix.load(mix.mRegX, 1, 0, 5);

        mix.convertToNumeric();
        assertEquals(-12977700, mix.mRegA.getQuantity());
        mix.increase(mix.mRegA, 1, 0);
        mix.convertToCharacters();
        assertEquals(30, mix.mRegA.getField(1));
        assertEquals(30, mix.mRegA.getField(2));
        assertEquals(31, mix.mRegA.getField(3));
        assertEquals(32, mix.mRegA.getField(4));
        assertEquals(39, mix.mRegA.getField(5));

        assertEquals(37, mix.mRegX.getField(1));
        assertEquals(37, mix.mRegX.getField(2));
        assertEquals(36, mix.mRegX.getField(3));
        assertEquals(39, mix.mRegX.getField(4));
        assertEquals(39, mix.mRegX.getField(5));
    }

    @Test
    public void test1() {
        Mix mix = new Mix();

        Executor executor = new Executor(mix);

        Instruction[] pgm = new Instruction[] {
                new Instruction(false, 1, 0, 5, "STZ"),
                new Instruction(false, 1, 0, 5, "ENNX"),
                new Instruction(false, 1, 0, 1, "STX"),
                new Instruction(false, 1, 0, 5, "SLAX"),
                new Instruction(false, 1, 0, 5, "ENNA"),
                new Instruction(false, 1, 0, 5, "INCX"),
                new Instruction(false, 1, 0, 5, "ENT1"),
                new Instruction(false, 1, 0, 5, "SRC"),
                new Instruction(false, 1, 0, 5, "ADD"),
                new Instruction(true, 1, 0, 5, "DEC1"),
                new Instruction(false, 1, 0, 5, "STZ"),
                new Instruction(false, 1, 0, 5, "CMPA"),
                new Instruction(true, 1, 1, 1, "MOVE"),
                new Instruction(false, 1, 0, 5, "NUM"),
                new Instruction(false, 1, 0, 5, "CHAR"),
                new Instruction(false, 1, 0, 5, "HLT"),
        };
        executor.execute(pgm);

        assertEquals(Word.MINUS, mix.mRegA.getSign());
        assertEquals(30, mix.mRegA.getField(1));
        assertEquals(30, mix.mRegA.getField(2));
        assertEquals(30, mix.mRegA.getField(3));
        assertEquals(30, mix.mRegA.getField(4));
        assertEquals(30, mix.mRegA.getField(5));

        assertEquals(Word.MINUS, mix.mRegX.getSign());
        assertEquals(31, mix.mRegX.getField(1));
        assertEquals(30, mix.mRegX.getField(2));
        assertEquals(30, mix.mRegX.getField(3));
        assertEquals(30, mix.mRegX.getField(4));
        assertEquals(30, mix.mRegX.getField(5));

        assertEquals(Word.PLUS, mix.mRegIx[1].getSign());
        assertEquals(3, mix.mRegIx[1].getQuantity());

        assertEquals(Word.PLUS, mix.mMemory.get(1).getSign());
        assertEquals(0, mix.mMemory.get(1).getQuantity());
        assertEquals(Word.PLUS, mix.mMemory.get(2).getSign());
        assertEquals(0, mix.mMemory.get(2).getQuantity());


        // LESS
        //1: +00000
        // A -90001
        // X -10000
        // I1 +00002

        // OVERFLOW ON
        //0: +00000
        //1: +00000
        //A -00000 X -10000
        //A
        //I1 +00002

        // 1: -10000
        //
        //A: -90000 X: -10000
        //I1: +00001





    }
}
