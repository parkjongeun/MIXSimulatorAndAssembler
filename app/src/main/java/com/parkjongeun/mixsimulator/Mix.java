
package com.parkjongeun.mixsimulator;


/**
 * Created by Parkjongeun on 2016. 9. 13..
 */

public class Mix {


    Register mA;
    Register mX;

    IndexRegister mI1;
    IndexRegister mI2;
    IndexRegister mI3;
    IndexRegister mI4;
    IndexRegister mI5;
    IndexRegister mI6;

    IndexRegister mJ;

    OverFlowToggle mOverFlowToggle;
    CompIndicator mCompIndicator;

    Memory memory;

    IO[] mIOUnit;


    void loadA(int addr, int l, int r) {

        mA.clear();



        memory.get(addr).getField(l);


    }
}
