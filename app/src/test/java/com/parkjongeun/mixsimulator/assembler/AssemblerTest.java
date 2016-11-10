package com.parkjongeun.mixsimulator.assembler;

import com.parkjongeun.mixsimulator.Executor;
import com.parkjongeun.mixsimulator.Memory;
import com.parkjongeun.mixsimulator.Mix;

import org.junit.Test;

import static  org.junit.Assert.*;

/**
 * Created by Parkjongeun on 10/11/2016.
 */

public class AssemblerTest {

    @Test
    public void load() {

        Assembler assembler = new Assembler();
        //List<Assembler.Line> lines = assembler.parse(assembler.mPgm);

        Memory mem = assembler.assemble();
        System.out.println();

        Mix mix = new Mix();
        mix.mMemory = mem;
        Executor executor = new Executor(mix);
        executor.start(3000);

        assertEquals(321, 321);
        //mix.mMemory.get(4000).setQuantity(Word.MINUS, 432);
    }

}
