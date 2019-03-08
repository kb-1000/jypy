package com.github.kb1000.jypy;

public class PyObject {
    public PyType __class__;

    public PyObject __add__(PyObject other) throws JyPyException {
        throw new JyPyException(); // FIXME(kb1000): add CPython-like exception
    }

    public PyObject __getattribute__(PyObject attr) throws JyPyException {
        if (!Py.isinstance(attr, PyUnicode.TYPE)) {
            throw new JyPyException(); // FIXME(kb1000): add CPython-like exception
        }
        return null; // FIXME(kb1000)
    }
}
