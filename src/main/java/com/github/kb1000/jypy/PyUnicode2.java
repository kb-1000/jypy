package com.github.kb1000.jypy;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

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
     * get undefined and very likely unexpected and unwanted behavior.
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

    public static PyUnicode2 fromUCS2(String ucs2) {
        ByteBuffer buf = ByteBuffer.allocate(ucs2.length() * 2);
        buf.asCharBuffer().put(CharBuffer.wrap(ucs2.toCharArray())); // TODO(kb1000): test whether this actually works, on BE and on LE
        return new PyUnicode2(new ByteBuffer[] { buf }, new int[] { 2 });
    }

    public static PyUnicode2 fromUTF16(String utf16) {
        char[] chars = utf16.toCharArray();
        int len = chars.length;
        ByteBuffer buf = ByteBuffer.allocate(len * 2);
        boolean hasSurrogates = false;
        int i = 0;
        for (; i < len; i++) {
            char character = chars[i];
            if (Character.isHighSurrogate(character)) {
                hasSurrogates = true;
                break;
            }
            buf.putShort(i*2, (short) character);
        }
        if (!hasSurrogates) {
            return new PyUnicode2(new ByteBuffer[] { buf }, new int[] { 2 });
        } else {
            List<ByteBuffer> slices = new ArrayList<>();
            int start = 0;
            boolean startsWithSurrogate = i == 0;
            // TODO(kb1000): check correctness of indices here
            for (; i < len; i++) {
                char character = chars[i];
                if (Character.isHighSurrogate(character)) {
                    char character2 = chars[i+1];
                    if (Character.isLowSurrogate(character2)) {
                        if (i != 0) {
                            slices.add(buf.duplicate().position(start * 2).limit(i * 2).slice());
                            start = i;
                        }
                        buf.putInt(i * 2, Character.toCodePoint(character, character2));
                        i += 2;
                        while (Character.isHighSurrogate(chars[i]) && Character.isLowSurrogate(chars[i+1])) {
                            buf.putInt(i * 2, Character.toCodePoint(chars[i], chars[i + 1])); // TODO(kb1000): read the characters only once instead of twice
                            i += 2;
                        }
                        slices.add(buf.duplicate().position(start * 2).limit(i * 2).slice());
                        start = i;
                        i--;
                    } else {
                        buf.putShort(i++ * 2, (short) character);
                        buf.putShort(i * 2, (short) character2);
                    }
                } else {
                    buf.putShort(i * 2, (short) character);
                }
            }
            if (start != i) slices.add(buf.duplicate().position(start * 2).limit(i * 2).slice());
            final int slice_count = slices.size();
            int[] kinds = new int[slice_count];
            if (startsWithSurrogate) {
                final int j_max = slice_count + 1;
                for (int j = 1; j < j_max; j++) {
                    kinds[j-1] = ((j % 2) + 1) * 2;
                }
            } else {
                for (int j = 0; j < slice_count; j++) {
                    kinds[j] = ((j % 2) + 1) * 2;
                }
            }
            return new PyUnicode2(slices.toArray(new ByteBuffer[slice_count]), kinds);
        }
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
                result = ByteBuffer.allocate(resultLen * resultKind);
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
                        while (shortBuffer.hasRemaining()) {
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
