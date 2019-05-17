package com.github.kb1000.jypy;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.github.kb1000.jypy.common.Array;

public class PyUnicode2 extends PyObject {
    private ByteBuffer[] buffers;
    private int[] kinds;
    private static final Object lock = new Object();
    //public static final int MAXIMUM_HEAP = 4096;
    public static final int MAXIMUM_ROPE_ELEMENTS = 10;

    /**
     * When you modify this, you might waste memory by copying the data again.
     * If you set this to {@code true}, when it should be {@code false}, you will
     * get undefined behavior and very likely unexpected and unwanted behavior.
     */
    private boolean evaluated = false;

    public PyUnicode2() {
        buffers = new ByteBuffer[0];
        kinds = new int[0];
    }

    private PyUnicode2(ByteBuffer[] buffers, int[] kinds) {
        this.buffers = buffers;
        this.kinds = kinds;
    }

    private void evaluate() {
        if (evaluated) return;
        synchronized (lock) {
            if (evaluated) return;
            int len = buffers.length;
            if (len != kinds.length) throw new IllegalArgumentException("length of kinds array does not match length of buffers array");
            int resultKind = 1;
            for (int kind: kinds) {
                if (kind > resultKind) {
                    resultKind = kind;
                }
            }
            int[] localKinds = kinds;
            ByteBuffer[] localBuffers = buffers;
            int resultLen = 0;
            for (int i = 0; i < len; i++) {
                resultLen += localBuffers[i].capacity() / localKinds[i];
            }
            ByteBuffer result;
            //if (resultLen < MAXIMUM_HEAP) {
                result = ByteBuffer.allocate(resultLen);
            //} else {
            //    result = ByteBuffer.allocateDirect(resultLen);
            //}
            switch (resultKind) {
            case 1:
                for (ByteBuffer buffer: localBuffers) {
                    result.put(buffer);
                }
                result.rewind();
                break;
	    case 2:
                ShortBuffer shortResult = result.asShortBuffer();
                for (int i = 0; i < len; i++) {
                    ByteBuffer buffer = localBuffers[i].asReadOnlyBuffer(); // to prevent side effects from modifying the position
                    int kind = kinds[i];
                    switch (kind) {
                    case 1:
                        while (buffer.hasRemaining()) {
                            shortResult.put((short)(buffer.get() & ((short) 0xFF)));
                        }
                        break;
                    case 2:
                        shortResult.put(buffer.asShortBuffer());
                        break;
                    default:
                        throw new IllegalArgumentException("kind of buffer no. " + i + " is " + kind + ", but expected 1 or 2");
                    }
                }
                break;
            case 4:
                IntBuffer intResult = result.asIntBuffer();
                for (int i = 0; i < len; i++) {
                    ByteBuffer buffer = localBuffers[i].asReadOnlyBuffer(); // to prevent side effects from modifying the position
                    int kind = localKinds[i];
                    switch (kind) {
                    case 1:
                        while (buffer.hasRemaining()) {
                            intResult.put(buffer.get() & ((int) 0xFF));
                        }
                        break;
                    case 2:
                        ShortBuffer shortBuffer = buffer.asShortBuffer();
                        while (buffer.hasRemaining()) {
                            intResult.put(shortBuffer.get() & ((int) 0xFFFF));
                        }
                        break;
                    case 4:
                        intResult.put(buffer.asIntBuffer());
                        break;
                    default:
                        throw new IllegalArgumentException("kind of buffer no. " + i + " is " + kind + ", but expected 1, 2 or 4");
                    }
                }
                break;
            }
            kinds = new int[] { resultKind };
            buffers = new ByteBuffer[] { result };
        }
    }

    @Override
    public PyObject __add__(Object other) {
        if (!(other instanceof PyUnicode2)) return Py.NotImplemented;
        PyUnicode2 other2 = (PyUnicode2) other;
        synchronized (lock) {
            PyUnicode2 newUnicode = new PyUnicode2(Array.concat(buffers, other2.buffers), Array.concat(kinds, other2.kinds));
            if (newUnicode.buffers.length >= MAXIMUM_ROPE_ELEMENTS) {
                newUnicode.evaluate();
            }
            return newUnicode;
        }
    }
}
