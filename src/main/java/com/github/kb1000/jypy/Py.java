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
            return PyLong.from((int) it);
        } else if (it instanceof Short) {
            return PyLong.from((short) it);
        } else if (it instanceof Long) {
            return PyLong.from((long) it);
        } else if (it instanceof Byte) {
            return PyLong.from((byte) it);
        } else if (it instanceof Double) {
            return PyFloat.from((double) it);
        } else if (it instanceof Float) {
            return PyFloat.from((float) it);
        } else if (it instanceof String) {
            return PyUnicode.from((String) it);
        } else if (it instanceof Boolean) {
            return PyBool.from((boolean) it);
        }
        return null; // FIXME(kb1000)
    }

    public static Object add(Object first, Object second) throws JyPyException {
        Object ret = null;
        if (first instanceof PyObject) {
            PyObject pyFirst = (PyObject) first;
            if ((ret = pyFirst.__add__(second)) != NotImplemented) {
                return ret;
            }
        }
        if (second instanceof PyObject) {
            PyObject pySecond = (PyObject) second;
            if ((ret = pySecond.__radd__(first)) != NotImplemented) {
                return ret;
            }
        }
        if (ret == NotImplemented) {
            throw new JyPyException(); // FIXME(kb1000): add CPython-like exception
        }
        return null; //FIXME(kb1000)
    }

    public static final PyNotImplemented NotImplemented = PyNotImplemented.INSTANCE;
    public static final PyNone None = PyNone.INSTANCE;
    public static final PyEllipsis Ellipsis = PyEllipsis.INSTANCE;
}
