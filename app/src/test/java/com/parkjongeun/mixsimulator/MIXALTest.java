package com.parkjongeun.mixsimulator;

import com.parkjongeun.mixsimulator.assembler.APart;
import com.parkjongeun.mixsimulator.assembler.AtomicExpression;
import com.parkjongeun.mixsimulator.assembler.Expression;
import com.parkjongeun.mixsimulator.assembler.FPart;
import com.parkjongeun.mixsimulator.assembler.IndexPart;
import com.parkjongeun.mixsimulator.assembler.LiteralConstant;
import com.parkjongeun.mixsimulator.assembler.Number;
import com.parkjongeun.mixsimulator.assembler.Symbol;
import com.parkjongeun.mixsimulator.assembler.SymbolTable;
import com.parkjongeun.mixsimulator.assembler.SymbolTableImpl;
import com.parkjongeun.mixsimulator.assembler.WValue;

import org.junit.Test;

import static  org.junit.Assert.*;

/**
 * Created by Parkjongeun on 08/10/2016.
 */

public class MIXALTest {

    @Test
    public void load() {

        /*Assembler assembler = new Assembler();
        //List<Assembler.Line> lines = assembler.parse(assembler.mPgm);

        Memory mem = assembler.assemble();

        Mix mix = new Mix();
        mix.mMemory = mem;
        Executor executor = new Executor(mix);
        executor.start(3000);

        assertEquals(321, 321);*/
        //mix.mMemory.get(4000).setQuantity(Word.MINUS, 432);
    }

    @Test
    public void isSymbol() {
        assertTrue(Symbol.isSymbol("20BY20"));
        assertTrue(Symbol.isSymbol("PRIME"));
        assertTrue(Symbol.isSymbol("TEMP"));
        assertTrue(Symbol.isSymbol("Z"));

        assertFalse(Symbol.isSymbol("7438234"));
        assertFalse(Symbol.isSymbol("JFKWODKWSKA"));
        assertFalse(Symbol.isSymbol("PRiME"));
        assertFalse(Symbol.isSymbol("1"));
        assertFalse(Symbol.isSymbol(""));
    }

    @Test
    public void isNumber() {
        assertTrue(Number.isNumber("00032123"));
        assertTrue(Number.isNumber("123"));
        assertTrue(Number.isNumber("9"));
        assertTrue(Number.isNumber("0"));

        assertFalse(Number.isNumber("123A"));
        assertFalse(Number.isNumber(""));
        assertFalse(Number.isNumber("58392039302"));
    }

    @Test
    public void isAtomicSymbol() {
        SymbolTable st = new SymbolTableImpl();
        st.add("A00032123", 0);
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertTrue(AtomicExpression.isAtomicExpression("00032123", st));
        assertTrue(AtomicExpression.isAtomicExpression("ASB", st));
        assertTrue(AtomicExpression.isAtomicExpression("23SF", st));
        assertFalse(AtomicExpression.isAtomicExpression("23SFA", st));
        assertTrue(AtomicExpression.isAtomicExpression("*", st));
        assertFalse(AtomicExpression.isAtomicExpression("**", st));
        assertFalse(AtomicExpression.isAtomicExpression("?", st));
        assertFalse(AtomicExpression.isAtomicExpression("", st));
        assertTrue(AtomicExpression.isAtomicExpression("0", st));
        assertFalse(AtomicExpression.isAtomicExpression("14678923783", st));
    }

    @Test
    public void isExpression() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertTrue(Expression.isExpression("00032123", st));
        System.out.println();
        assertTrue(Expression.isExpression("-00032123**/4323", st));
        System.out.println();
        assertTrue(Expression.isExpression("00032123+432423/4323", st));
        System.out.println();
        assertTrue(Expression.isExpression("-*:6****", st));
        System.out.println();
        assertFalse(Expression.isExpression("**", st));
        System.out.println();
    }

    @Test
    public void isAPart() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertTrue(APart.isAPart("", st));
        assertTrue(APart.isAPart("42+452/ASB:234", st));
        assertTrue(APart.isAPart("42+234/23SF", st));
        assertTrue(APart.isAPart("2F", st));
        assertTrue(APart.isAPart("=42+234/23SF=", st));
    }

    @Test
    public void isIndexPart() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertTrue(IndexPart.isIndexPart("", st));
        assertTrue(IndexPart.isIndexPart(",323423", st));
        assertTrue(IndexPart.isIndexPart(",3243+4532/432", st));
        assertTrue(IndexPart.isIndexPart(",ASB+23SF/23", st));
        assertFalse(IndexPart.isIndexPart("ASB+23SF/23", st));
    }

    @Test
    public void isFPart() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertTrue(FPart.isFPart("", st));
        assertTrue(FPart.isFPart("(234)", st));
        assertTrue(FPart.isFPart("(423+42-423)", st));
        assertTrue(FPart.isFPart("(1:5)", st));
        assertFalse(FPart.isFPart("(JWIW)", st));
        assertFalse(FPart.isFPart("(2F)", st));
    }

    @Test
    public void isWValue() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertFalse(WValue.isWValue("", st));
        assertTrue(WValue.isWValue("2344", st));
        assertTrue(WValue.isWValue("ASB", st));
        assertTrue(WValue.isWValue("ASB(1:5)", st));
        assertTrue(WValue.isWValue("ASB(1:5),23SF,32432(123+342/321),23SF(2:4)", st));
    }

    @Test
    public void isLiteralConstant() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 0);
        st.add("ASB", 0);

        assertFalse(LiteralConstant.isLiteralContant("", st));
        assertFalse(LiteralConstant.isLiteralContant("321", st));
        assertFalse(LiteralConstant.isLiteralContant("==", st));
        assertTrue(LiteralConstant.isLiteralContant("=3212=", st));
        assertTrue(LiteralConstant.isLiteralContant("=ASB(1:5),23SF,32432(123+342/321),23SF(2:4)=", st));
    }

    @Test
    public void evalAtomicExpression() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 1238);
        st.add("ASB", 6879);

        assertEquals(342123, AtomicExpression.eval("342123", st, 0));
        assertEquals(1234, AtomicExpression.eval("*", st, 1234));
        assertEquals(6879, AtomicExpression.eval("ASB", st, 1000));
        assertEquals(1238, AtomicExpression.eval("23SF", st, 1234));
    }

    @Test
    public void evalExpression() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 1238);
        st.add("ASB", 6879);

        assertEquals(3, Expression.eval("1+2", st, 0));
        assertEquals(-1, Expression.eval("1-2", st, 1234));
        assertEquals(2, Expression.eval("1*2", st, 1000));
        assertEquals(0, Expression.eval("1/2", st, 1000));

        assertEquals(1123 + 2432, Expression.eval("1123+2432", st, 0));
        assertEquals(1234 - 2432, Expression.eval("1234-2432", st, 1234));
        assertEquals(1234 * 2432, Expression.eval("1234*2432", st, 1000));
        assertEquals(1234 / 2432, Expression.eval("1234/2432", st, 1000));


        assertEquals(1234 - 543, Expression.eval("*-543", st, 1234));
        assertEquals(0, Expression.eval("*-*", st, 1234));
        assertEquals(1234 * 1234, Expression.eval("***", st, 1234));

        assertEquals(((321*234)/23)+543, Expression.eval("321*234/23+543", st, 1234));
        assertEquals((((321*234)/23)+543)-1234, Expression.eval("321*234/23+543-*", st, 1234));

        assertEquals((((6879*234)/6879)+543)-1234, Expression.eval("ASB*234/ASB+543-*", st, 1234));
        assertEquals((((123*234)/6879)+1238)-1234, Expression.eval("123*234/ASB+23SF-*", st, 1234));

        assertNotEquals(123+234/79+1238, Expression.eval("123+234/79+23SF", st, 1234));
    }

    @Test
    public void evalIndexPart() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 1238);
        st.add("ASB", 6879);

        assertEquals(3, IndexPart.eval(",1+2", st, 0));
        assertEquals(-1, IndexPart.eval(",1-2", st, 1234));
        assertEquals(2, IndexPart.eval(",1*2", st, 1000));
        assertEquals(0, IndexPart.eval(",1/2", st, 1000));

        assertEquals(1123 + 2432, IndexPart.eval(",1123+2432", st, 0));
        assertEquals(1234 - 2432, IndexPart.eval(",1234-2432", st, 1234));
        assertEquals(1234 * 2432, IndexPart.eval(",1234*2432", st, 1000));
        assertEquals(1234 / 2432, IndexPart.eval(",1234/2432", st, 1000));


        assertEquals(1234 - 543, IndexPart.eval(",*-543", st, 1234));
        assertEquals(0, IndexPart.eval(",*-*", st, 1234));
        assertEquals(1234 * 1234, IndexPart.eval(",***", st, 1234));

        assertEquals(((321*234)/23)+543, IndexPart.eval(",321*234/23+543", st, 1234));
        assertEquals((((321*234)/23)+543)-1234, IndexPart.eval(",321*234/23+543-*", st, 1234));

        assertEquals((((6879*234)/6879)+543)-1234, IndexPart.eval(",ASB*234/ASB+543-*", st, 1234));
        assertEquals((((123*234)/6879)+1238)-1234, IndexPart.eval(",123*234/ASB+23SF-*", st, 1234));
        //assertNotEquals((((123*234)/6879)+1238)-1234, IndexPart.eval("123*234/ASB+23SF-*", st, 1234));

        assertNotEquals(123+234/79+1238, IndexPart.eval(",123+234/79+23SF", st, 1234));

    }

    @Test
    public void evalFPart() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 1238);
        st.add("ASB", 6879);

        final int NORMAL_SETTING = 5;

        assertEquals(3, FPart.eval("(1+2)", st, 0, NORMAL_SETTING));
        assertEquals(-1, FPart.eval("(1-2)", st, 1234, NORMAL_SETTING));
        assertEquals(2, FPart.eval("(1*2)", st, 1000, NORMAL_SETTING));
        assertEquals(0, FPart.eval("(1/2)", st, 1000, NORMAL_SETTING));

        assertEquals(1123 + 2432, FPart.eval("(1123+2432)", st, 0, NORMAL_SETTING));
        assertEquals(1234 - 2432, FPart.eval("(1234-2432)", st, 1234, NORMAL_SETTING));
        assertEquals(1234 * 2432, FPart.eval("(1234*2432)", st, 1000, NORMAL_SETTING));
        assertEquals(1234 / 2432, FPart.eval("(1234/2432)", st, 1000, NORMAL_SETTING));


        assertEquals(1234 - 543, FPart.eval("(*-543)", st, 1234, NORMAL_SETTING));
        assertEquals(0, FPart.eval("(*-*)", st, 1234, NORMAL_SETTING));
        assertEquals(1234 * 1234, FPart.eval("(***)", st, 1234, NORMAL_SETTING));

        assertEquals(((321*234)/23)+543, FPart.eval("(321*234/23+543)", st, 1234, NORMAL_SETTING));
        assertEquals((((321*234)/23)+543)-1234, FPart.eval("(321*234/23+543-*)", st, 1234, NORMAL_SETTING));

        assertEquals((((6879*234)/6879)+543)-1234, FPart.eval("(ASB*234/ASB+543-*)", st, 1234, NORMAL_SETTING));
        assertEquals((((123*234)/6879)+1238)-1234, FPart.eval("(123*234/ASB+23SF-*)", st, 1234, NORMAL_SETTING));
        //assertNotEquals((((123*234)/6879)+1238)-1234, IndexPart.eval("123*234/ASB+23SF-*", st, 1234));

        assertNotEquals(123+234/79+1238, FPart.eval("(123+234/79+23SF)", st, 1234, NORMAL_SETTING));
    }

    @Test
    public void assembleWValue() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 1238);
        st.add("ASB", 6879);

        assertEquals(1, WValue.assemble("1", st, 0).getQuantity());
        assertEquals(3, WValue.assemble("1,2,3", st, 0).getQuantity());
        assertEquals(1 + 1 * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE, WValue.assemble("1,1(1:1)", st, 0).getQuantity());
        assertEquals(-1 + -1 * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE, WValue.assemble("1,-1(0:1)", st, 0).getQuantity());
        assertEquals(1, WValue.assemble("-1000(0:2),1", st, 0).getQuantity());
        assertEquals(-1, WValue.assemble("-1000(0:2),-1", st, 0).getQuantity());
    }

    @Test
    public void assembleLiteralConstant() {
        SymbolTable st = new SymbolTableImpl();
        st.add("23SF", 1238);
        st.add("ASB", 6879);

        assertEquals(1, LiteralConstant.assemble("=1=", st, 0).getQuantity());
        assertEquals(3, LiteralConstant.assemble("=1,2,3=", st, 0).getQuantity());
        assertEquals(1 + 1 * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE, LiteralConstant.assemble("=1,1(1:1)=", st, 0).getQuantity());
        assertEquals(-1 + -1 * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE * Word.BYTE_SIZE, LiteralConstant.assemble("=1,-1(0:1)=", st, 0).getQuantity());
        assertEquals(1, LiteralConstant.assemble("=-1000(0:2),1=", st, 0).getQuantity());
        assertEquals(-1, LiteralConstant.assemble("=-1000(0:2),-1=", st, 0).getQuantity());
    }
}
