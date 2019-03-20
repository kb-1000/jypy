package com.github.kb1000.jypy;

class PyFloat extends PyObject {
    public final double value;

    private PyFloat(double value) {
        this.value = value;
    }

    public static PyFloat from(double value) {
        return new PyFloat(value);
    }

    @Override
    public PyObject __add__(PyObject other) throws JyPyException {
        if (other instanceof PyFloat) {
            return from(value + ((PyFloat) other).value);
        }
        else if (other instanceof PyLong) {
            double otherDouble = ((PyLong) other).bigInteger.doubleValue();
            if (Double.isInfinite(otherDouble)) {
                throw new JyPyException(); // new PyOverflow("int too large to convert to float") // FIXME(kb1000)
            }
            return from(value + otherDouble);
            
        }
        return Py.NotImplemented;
    }
}
