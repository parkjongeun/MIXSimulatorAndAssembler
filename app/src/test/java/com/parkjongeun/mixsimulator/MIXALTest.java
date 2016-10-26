package com.parkjongeun.mixsimulator;

import org.junit.Test;

import java.util.List;

import static  org.junit.Assert.*;

/**
 * Created by jepark on 08/10/2016.
 */

public class MIXALTest {

    @Test
    public void load() {

        Assembler assembler = new Assembler();
        //List<Assembler.Line> lines = assembler.parse(assembler.mPgm);

        Memory mem = assembler.assemble();

        Mix mix = new Mix();
        mix.mMemory = mem;
        Executor executor = new Executor(mix);
        executor.start(3000);

        assertEquals(321, 321);
        //mix.mMemory.get(4000).setQuantity(Word.MINUS, 432);
    }
}
