package com.parkjongeun.mixsimulator.assembler;

import com.parkjongeun.mixsimulator.Word;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public interface SymbolTable {

    boolean contains(String symbol);
    void add(String symbol, int value);
    int get(String symbol);
}
