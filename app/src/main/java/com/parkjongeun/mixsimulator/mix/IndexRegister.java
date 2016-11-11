package com.parkjongeun.mixsimulator.mix;

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
    public void setQuantity(int sign, int quantity) {
        if (Math.abs(quantity) >= Word.BYTE_SIZE * Word.BYTE_SIZE) {
            throw new IllegalArgumentException("Doesn't fit in two bytes: " + quantity);
        }
        super.setQuantity(sign, quantity);
    }
}
