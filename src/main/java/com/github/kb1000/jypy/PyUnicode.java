package com.github.kb1000.jypy;

//FIXME(kb1000): This class does not support UTF-32 chars without surrogates, while they are always natively supported in newer Python 3 versions.
public class PyUnicode {
    public final String string;
    public static final PyType TYPE = null; // FIXME(kb1000)
    private PyUnicode(String string) {
        this.string = string;
    }

    public static final PyUnicode EMPTY = new PyUnicode("");

    public static PyUnicode from(String string) {
        if (!string.isEmpty()) {
            return new PyUnicode(string);
        } else {
            return EMPTY;
        }
    }

    @Override
    public PyObject __add__(PyObject other) {
        if (other instanceof PyUnicode) {
            return from(string + ((PyUnicode) other).string);
        }
        return Py.NotImplemented;
    }
}
