package com.parkjongeun.mixsimulator.simulator.io;

import com.parkjongeun.mixsimulator.Memory;
import com.parkjongeun.mixsimulator.Word;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public abstract class IO {

    private AtomicBoolean mIsBusy = new AtomicBoolean(false);

    // MIX Thread
    private final Thread mMIXThread;


    public IO(Thread mixThread) {
        mMIXThread = mixThread;
    }

    public final void input(final Memory mem, final int startingAt) {
        checkIsMIXThread();
        checkIsNotBusy();
        checkIsInputDevice();

        if (!mIsBusy.compareAndSet(false, true)) {
            throw new IllegalArgumentException("Assertion failed.");
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Word[] block = in();

                    for (int src = 0, dst = startingAt; src < blockSize(); ++src, ++dst) {
                        mem.write(dst, block[src]);
                    }
                } finally {
                    if (!mIsBusy.compareAndSet(true, false)) {
                        throw new IllegalArgumentException("Assertion failed.");
                    }
                }
            }
        });

        thread.start();
    }


    public final void output(final Memory mem, final int startingAt) {
        checkIsMIXThread();
        checkIsNotBusy();
        checkIsOutputDevice();

        if (!mIsBusy.compareAndSet(false, true)) {
            throw new IllegalArgumentException("Assertion failed.");
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Word[] block = new Word[blockSize()];
                    for (int src = startingAt, dst = 0; src < startingAt + blockSize(); ++src, ++dst) {
                        Word w = mem.read(src);
                        block[dst] = w;
                    }
                    out(block);
                } finally {
                    if (!mIsBusy.compareAndSet(true, false)) {
                        throw new IllegalArgumentException("Assertion failed.");
                    }
                }
            }
        });

        thread.start();
    }

    public final void iOControl(final int m) {
        checkIsMIXThread();
        checkIsNotBusy();

        if (!mIsBusy.compareAndSet(false, true)) {
            throw new IllegalArgumentException("Assertion failed.");
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ioc(m);
                } finally {
                    if (!mIsBusy.compareAndSet(true, false)) {
                        throw new IllegalArgumentException("Assertion failed.");
                    }
                }
            }
        });

        thread.start();
    }

    protected abstract void out(Word[] block);

    protected abstract Word[] in();

    protected abstract void ioc(int m);

    protected abstract int blockSize();

    protected abstract boolean isInputDevice();

    public final boolean isReady() {
        return !mIsBusy.get();
    }

    public void waitUntilReady() {
        checkIsMIXThread();

        while (mIsBusy.get()) {
            // TODO: Improve!
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {

            }
        }
    }

    private void checkIsMIXThread() {
        if (mMIXThread != Thread.currentThread()) {
            throw new IllegalArgumentException("mMIXThread != Thread.currentThread()");
        }
    }

    private void checkIsNotBusy() {
        if (mIsBusy.get()) {
            throw new IllegalArgumentException("It's busy.");
        }
    }

    private void checkIsOutputDevice() {
        if (isInputDevice()) {
            throw new IllegalArgumentException("This is a input device.");
        }
    }

    private void checkIsInputDevice() {
        if (!isInputDevice()) {
            throw new IllegalArgumentException("This is a output device.");
        }
    }

}
