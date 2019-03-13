package com.github.kb1000.jypy;

import java.math.BigInteger;

public final class Py {
    public static boolean isinstance(PyObject object, PyType type) {
        return false; // FIXME(kb1000)
    }

    public static PyObject pyReWrap(Object it) throws JyPyException {
        if (it == null) {
            return None;
        } else if (it instanceof BigInteger) {
            return PyLong.from((BigInteger) it);
        } else if (it instanceof Integer) {
            return PyLong.from((Integer) it);
        } else if (it instanceof Short) {
            return PyLong.from((Short) it);
        } else if (it instanceof Long) {
            return PyLong.from((Long) it);
        } else if (it instanceof Byte) {
            return PyLong.from((Byte) it);
        } else if (it instanceof Double) {
            return PyFloat.from((Double) it);
        } else if (it instanceof Float) {
            return PyFloat.from((Float) it);
        } else if (it instanceof String) {
            return PyUnicode.from((String) it);
        }
        return null; // FIXME(kb1000)
    }

    public static final PyNotImplemented NotImplemented = PyNotImplemented.INSTANCE;
    public static final PyNone None = PyNone.INSTANCE;
    public static final PyEllipsis Ellipsis = PyEllipsis.INSTANCE;
}
