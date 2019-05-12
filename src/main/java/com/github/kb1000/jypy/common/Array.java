package com.github.kb1000.jypy.common;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class Array {
    private Array() {
        throw new UnsupportedOperationException();
    }

    public static int[] concat(int[] a, int[] b) {
        if (a.length == 0) {
            return Arrays.copyOf(b, b.length);
        } else if (b.length == 0) {
            return Arrays.copyOf(a, a.length);
        }
        int totalLength = a.length + b.length;
        int[] result = new int[totalLength];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static ByteBuffer[] concat(ByteBuffer[] a, ByteBuffer[] b) {
        if (a.length == 0) {
            return Arrays.copyOf(b, b.length);
        } else if (b.length == 0) {
            return Arrays.copyOf(a, a.length);
        }
        int totalLength = a.length + b.length;
        ByteBuffer[] result = new ByteBuffer[totalLength];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
