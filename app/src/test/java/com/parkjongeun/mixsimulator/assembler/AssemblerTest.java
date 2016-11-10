package com.parkjongeun.mixsimulator.assembler;

import com.parkjongeun.mixsimulator.Executor;
import com.parkjongeun.mixsimulator.Memory;
import com.parkjongeun.mixsimulator.Mix;
import com.parkjongeun.mixsimulator.util.Pair;

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

        Pair<Memory, Integer> pgm = assembler.assemble(pgm2);
        System.out.println();

        Mix mix = new Mix();
        mix.mMemory = pgm.first;
        Executor executor = new Executor(mix);
        executor.start(pgm.second);

        assertEquals(321, 321);
        //mix.mMemory.get(4000).setQuantity(Word.MINUS, 432);
    }

    static String pgm1 =
            " ORIG 3000\n" +
            "START OUT HELLO(18)\n" +
                    " HLT\n" +
                    " ORIG 1000\n" +
                    "HELLO ALF \"HELLO\"\n" +
                    " ALF \", WOR\"\n" +
                    " ALF \"LD.  \"\n" +
                    " ORIG 2000\n" +
                    " END START\n";

    static String pgm2 =
            " ORIG 3000\n" +
                    "START ENTA 123\n" +
                    " INCA 456\n" +
                    " CHAR \n" +
                    " STX SOLUTION(3:5)\n" +
                    " OUT SOLUTION(18)\n" +
                    " HLT\n" +
                    " ORIG 0\n" +
                    "SOLUTION CON 0\n" +
                    " ORIG 2000\n" +
                    " END START\n";
}
