package com.github.kb1000.jypy;

import java.math.BigInteger;

public final class Py {
    public static boolean isinstance(PyObject object, PyType type) {
        return false; // FIXME(kb1000)
    }

    public static PyObject pyReWrap(Object it) throws JyPyException {
        if (it instanceof BigInteger) {
            return PyLong.from((BigInteger) it);
        }
        return null; // FIXME(kb1000)
    }

    public static final PyNotImplemented NotImplemented = PyNotImplemented.INSTANCE;
}
