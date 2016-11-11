package com.parkjongeun.mixsimulator.assembler;


import java.util.List;
import java.util.Map;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public interface SymbolTable {

    boolean contains(String symbol);
    void add(String symbol, int value);
    int get(String symbol);
    //String getNextLocalSymbol(int d);
    void addFutureRef(int line, String symbol);
    List<String> getUndefinedFutureRefs();
    Map<Integer, String> getFutureRef();
}
