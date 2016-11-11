package com.parkjongeun.mixsimulator.assembler;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Parkjongeun on 01/11/2016.
 */

public class SymbolTableImpl implements SymbolTable {

    private Map<String, Integer> mMap = new HashMap<>();

    // TODO: Change to the Word type.
    //private int[] localSymbolLast = new int[10]; // 0 ~ 9

    private int[] localSymbolAllocator = new int[10]; // 0 ~ 9
    private int literalConstAllocator = 0;

    private static final String LOCAL_SYMBOL_PREFIX = "$local_symbol_";
    private static final String LITERAL_CONST_PREFIX = "$literal_const_";

    private Map<Integer, String> mFutureRef = new HashMap<>();

    private Map<Integer, Word> mLiteralConst = new HashMap<>();

    // State
    /*public String calcLocalSymbolFuture(String str) {
        if (!isLocalSymbolRefForward(str)) {
            throw new IllegalArgumentException();
        }
        int d = extractDigit(str);
        return LOCAL_SYMBOL_PREFIX + localSymbolAllocator[d];
    }*/

    @Override
    public Map<Integer, String> getFutureRef() {
        return mFutureRef;
    }

    @Override
    public List<String> getUndefinedFutureRefs() {
        Set<String> undefRefs = new TreeSet<>();
        Set<Integer> addresses = mFutureRef.keySet();
        for (Integer address : addresses) {
            String ref = mFutureRef.get(address);
            if (!mMap.containsKey(ref)) {
                // TODO: !
                if (ref.startsWith(LOCAL_SYMBOL_PREFIX)) {
                    throw new IllegalArgumentException();
                }
                if (!undefRefs.contains(ref)) {
                    undefRefs.add(ref);
                }
            }
        }
        return new ArrayList<>(undefRefs);
    }

    @Override
    public void addFutureRef(int line, String symbol) {
        if (!mFutureRef.containsKey(line)) {
            if (isLocalSymbolRefForward(symbol)) {
                int d = extractDigit(symbol);
                String lsym = getNextLocalSymbol(d);
                mFutureRef.put(line, lsym);
            } else {
                mFutureRef.put(line, symbol);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String nextLocalSymbol(int d) {
        String tmp = getNextLocalSymbol(d);
        localSymbolAllocator[d]++;
        return tmp;
    }

    private String getNextLocalSymbol(int d) {
        return LOCAL_SYMBOL_PREFIX + d + "_" + localSymbolAllocator[d];
    }

    private String getLastLocalSymbol(int d) {
        if (localSymbolAllocator[d] == 0) {
            throw new IllegalArgumentException();
        }
        return LOCAL_SYMBOL_PREFIX + d + "_" + (localSymbolAllocator[d]-1);
    }

    @Override
    public boolean contains(String symbol) {
        if (isLocalSymbolRefBackward(symbol)) {
            int d = extractDigit(symbol);
            String lsym = getLastLocalSymbol(d);
            return mMap.containsKey(lsym);
        } else {
            return mMap.containsKey(symbol);
        }
    }

    @Override
    public void add(String symbol, int value) {
        //if (!Symbol.isSymbol(symbol)) {
        //    throw new IllegalArgumentException();
        //}

        if (isLocalSymbol(symbol)) {
            int d = extractDigit(symbol);
            String lsym = nextLocalSymbol(d);
            if (!mMap.containsKey(lsym)) {
                mMap.put(lsym, value);
            } else {
                throw new IllegalArgumentException("");
            }
        } else {
            if (!mMap.containsKey(symbol)) {
                mMap.put(symbol, value);
            } else {
                throw new IllegalArgumentException("");
            }
        }
    }

    @Override
    public int get(String symbol) {
        if (isLocalSymbolRefBackward(symbol)) {
            int d = extractDigit(symbol);
            String lsym = getLastLocalSymbol(d);
            return mMap.get(lsym);
        } else if (mMap.containsKey(symbol)) {
            return mMap.get(symbol);
        } else {
            throw new NoSuchElementException();
        }
    }

    // 0H, 2H, 3H, 0F, 0B, 2F, 2B, ...
    static boolean isLocalSymbol(String sym) {
        if (sym.length() == 2) {
            if (isDigit(sym.charAt(0))) {
                if (sym.charAt(1) == 'H') // Here
                    return true;
            }
        }
        return false;
    }

    static boolean isLocalSymbolRefBackward(String str) {
        if (str.length() == 2) {
            if (isDigit(str.charAt(0))) {
                if (str.charAt(1) == 'B') // Backward
                    return true;
            }
        }
        return false;
    }

    static boolean isLocalSymbolRefForward(String str) {
        if (str.length() == 2) {
            if (isDigit(str.charAt(0))) {
                if (str.charAt(1) == 'F') // Backward
                    return true;
            }
        }
        return false;
    }

    static int extractDigit(String localSym) {
        return Integer.parseInt(localSym.substring(0, 1));
    }

    static boolean isDigit(char c) {
        return c == '0'
                || c == '1'
                || c == '2'
                || c == '3'
                || c == '4'
                || c == '5'
                || c == '6'
                || c == '7'
                || c == '8'
                || c == '9';
    }
}
