package com.parkjongeun.mixsimulator.assembler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class SymbolTableImpl implements SymbolTable {

    private Map<String, Integer> mMap = new HashMap<>();


    @Override
    public boolean contains(String symbol) {
        return mMap.containsKey(symbol);
    }

    @Override
    public void add(String symbol, int value) {
        if (!Symbol.isSymbol(symbol))
            throw new IllegalArgumentException();

        if (!mMap.containsKey(symbol)) {
            mMap.put(symbol, value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int get(String symbol) {
        if (mMap.containsKey(symbol)) {
            return mMap.get(symbol);
        } else {
            throw new NoSuchElementException();
        }
    }
}
