package com.github.kb1000.jypy;

public final class PyNotImplemented extends PyObject {
    private PyNotImplemented() {
        synchronized (PyNotImplemented.class) {
            if (!registerNotImplementedSingleton(this)) {
                throw new UnsupportedOperationException("Can not create more than one instance of PyNotImplemented!");
            }
        }
    }

    public static final PyNotImplemented INSTANCE = new PyNotImplemented();

    /**
     * This is used to prevent bad people from hackimg into this class using reflection and make more than one instance.
     * It will also be used to publish this instance to native code.
     *
     * @return whether there is already an instance
     */
    private static native boolean registerNotImplementedSingleton(PyNotImplemented instance);
}
