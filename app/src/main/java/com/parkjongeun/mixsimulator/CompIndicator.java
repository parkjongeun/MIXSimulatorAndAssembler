package com.parkjongeun.mixsimulator;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class CompIndicator {

    private int compIndi;

    boolean isEqual() {
        return compIndi == 1;
    }

    boolean isLess() {
        return compIndi == 2;
    }

    boolean isGreater() {
        return compIndi == 3;
    }

    void setEqual() {
         compIndi = 1;
    }

    void setLess() {
        compIndi = 2;
    }

    void setGreater() {
        compIndi = 3;
    }

    void clear() {
        compIndi = 0;
    }
}
