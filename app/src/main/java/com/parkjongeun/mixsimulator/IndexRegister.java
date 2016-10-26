package com.parkjongeun.mixsimulator;

import com.parkjongeun.mixsimulator.Register;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class IndexRegister extends Register {
    //public static int MAX_VALUE = BYTE_SIZE * BYTE_SIZE;

    public IndexRegister() {
        super();

        setField(1, 0);
        setField(2, 0);
        setField(3, 0);
    }

    @Override
    void setQuantity(int sign, int quantity) {
        if (Math.abs(quantity) >= BYTE_SIZE * BYTE_SIZE) {
            throw new IllegalArgumentException("Doesn't fit in two bytes: " + quantity);
        }
        super.setQuantity(sign, quantity);
    }
}
