package com.github.kb1000.jypy;

import java.math.BigInteger;

public class PyLong extends PyObject {
    public final BigInteger bigInteger;

    private PyLong(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public static PyLong from(BigInteger bigInteger) {
        return new PyLong(bigInteger); // TODO(kb1000): add pre-made objects for low numbers
    }

    @Override
    public PyObject __add__(PyObject other) {
        if (other instanceof PyLong) {
            return from(bigInteger.add(((PyLong) other).bigInteger));
        }
        return Py.NotImplemented;
    }
}
