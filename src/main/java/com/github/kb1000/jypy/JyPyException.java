package com.github.kb1000.jypy;

public class JyPyException extends Exception {
    // FIXME(kb1000): add constructors
    public JyPyException(Throwable t) {
        super(t);
    }

    public JyPyException() {
        super();
    }

    public boolean match(PyType type) {
        return false; // FIXME(kb1000)
    }
}
