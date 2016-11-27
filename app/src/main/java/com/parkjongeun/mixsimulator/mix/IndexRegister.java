package com.parkjongeun.mixsimulator.mix;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class IndexRegister extends Register {

    public static int MAX_ABS_VALUE = Word.getSizeOfByte() * Word.getSizeOfByte() - 1;

    public IndexRegister() {
        super();

        setField(1, 0);
        setField(2, 0);
        setField(3, 0);
    }

    @Override
    public void setQuantity(int sign, long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity < 0.");
        }
        if (quantity > MAX_ABS_VALUE) {
            throw new IllegalArgumentException("Doesn't fit in two bytes: " + quantity);
        }
        super.setQuantity(sign, quantity);
    }

    @Override
    public long getQuantity() {
        return super.getQuantity();
    }
}
