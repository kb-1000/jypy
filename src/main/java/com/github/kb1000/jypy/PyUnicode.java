package com.github.kb1000.jypy;

import java.util.Arrays;

import com.github.kb1000.jypy.annotations.NotInPython;
import com.github.kb1000.jypy.common.Array;

public class PyUnicode extends PyObject {
    private final int[] codePoints;
    private transient int[] publicCodePoints;
    public static final PyType TYPE = null; // FIXME(kb1000)
    @NotInPython(pyObject = true)
    public final int length;

    private PyUnicode(int[] codePoints) {
        this.codePoints = codePoints;
        this.length = codePoints.length;
    }

    public static final PyUnicode EMPTY = new PyUnicode(new int[0]);

    public static PyUnicode from(String string) {
        if (!string.isEmpty()) {
            return new PyUnicode(string.codePoints().toArray());
        } else {
            return EMPTY;
        }
    }

    private static PyUnicode fromNoCopy(int[] codePoints) {
        if (codePoints.length == 0) {
            return EMPTY;
        }
        return new PyUnicode(codePoints);
    }

    public static PyUnicode from(int[] codePoints) {
        return fromNoCopy(Arrays.copyOf(codePoints, codePoints.length));
    }

    @Override
    public PyObject __add__(PyObject other) {
        if (other instanceof PyUnicode) {
            return fromNoCopy(Array.concat(codePoints, ((PyUnicode) other).codePoints));
        }
        return Py.NotImplemented;
    }

    @NotInPython(pyObject = true)
    public synchronized int[] getCodePoints() {
        if (publicCodePoints == null) {
            publicCodePoints = Arrays.copyOf(codePoints, codePoints.length);
        }
        return publicCodePoints;
    }
}
