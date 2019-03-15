package com.github.kb1000.jypy;

public class PyObject {
    public PyType __class__;

    public PyObject __add__(final PyObject other) throws JyPyException {
        throw new JyPyException(); // FIXME(kb1000): add CPython-like exception
    }

    public PyObject __getattribute__(final PyObject attr) throws JyPyException {
        PyObject value = null;

        try {
            value = __class__.__getattribute__(attr);
        } catch (JyPyException e) {
            if (!e.match(PyAttributeError.TYPE)) {
                throw e;
            }
        }

        if (value == null) {
            throw new JyPyException(); // FIXME(kb1000): add CPython-like exception
        }

        // FIXME(kb1000): Add the other ways of getting an attribute and prevent infinite recursion

        return value;
    }

    public PyObject __call__(PyList args, PyDict kwargs) throws JyPyException { // FIXME(kb1000): Change to PyTuple after creating it
        throw new JyPyException(); // FIXME(kb1000): Add CPython-like exception
    }
}
