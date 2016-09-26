package com.parkjongeun.mixsimulator;

import com.parkjongeun.mixsimulator.Register;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class IndexRegister extends Register {
    public IndexRegister() {
        super();

        setField(1, 0);
        setField(2, 0);
        setField(3, 0);
    }

    @Override
    void setQuantity(int sign, int quantity) {
        if (quantity >= BYTE_SIZE * BYTE_SIZE) {
            throw new IllegalArgumentException("Doesn't fit in two bytes.");
        }
        super.setQuantity(sign, quantity);
    }
}
