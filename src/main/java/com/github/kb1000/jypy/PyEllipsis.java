package com.github.kb1000.jypy;

public final class PyEllipsis extends PyObject {
    private PyEllipsis() {
        synchronized (PyEllipsis.class) {
            if (!registerEllipsisSingleton(this)) {
                throw new UnsupportedOperationException("Can not create more than one instance of PyEllipsis!");
            }
        }
    }

    public static final PyEllipsis INSTANCE = new PyEllipsis();

    /**
     * This is used to prevent bad people from hackimg into this class using reflection and make more than one instance.
     * It will also be used to publish this instance to native code.
     *
     * @return whether there is already an instance
     */
    private static native boolean registerEllipsisSingleton(PyEllipsis instance);
}
