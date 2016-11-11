package com.parkjongeun.mixsimulator.mix;

/**
 * Created by Parkjongeun on 02/10/2016.
 */

public class Loader {

    private Mix mMix;


    public Loader(Mix mix) {
        mMix = mix;
    }

    public void loadAt(int address, Instruction[] instructions) {

        for (int i = 0; i < instructions.length; ++i) {
            mMix.mMemory.write(address + i, instructions[i].toWord());
        }

    }

}
