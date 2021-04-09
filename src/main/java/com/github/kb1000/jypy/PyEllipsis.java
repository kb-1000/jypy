package com.github.kb1000.jypy;

public final class PyEllipsis extends PyObject {
    /**
     * Do not create any additional instances. Doing so may corrupt the interpreter state, and <strong>will</strong>
     * cause wrong behavior on the newly created instance. You have been warned.
     */
    private PyEllipsis() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException("Can not create more than one instance of PyEllipsis!");
        }
    }

    public static final PyEllipsis INSTANCE = new PyEllipsis();
}
