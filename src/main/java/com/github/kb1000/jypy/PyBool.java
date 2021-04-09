package com.github.kb1000.jypy;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.github.kb1000.jypy.annotations.NotInPython;

public final class PyBool extends PyLong {
    /**
     * Do not create any additional instances. Doing so may corrupt the interpreter state, and <strong>will</strong>
     * cause wrong behavior on the newly created instance. You have been warned.
     */
    private PyBool(boolean value) {
        super(BigInteger.valueOf(value ? 1 : 0));
    }

    @NotInPython(pyObject = true)
    public boolean toBoolean() {
        return this == True;
    }

    public static final PyBool True = new PyBool(true);
    public static final PyBool False = new PyBool(false);

    public static PyBool from(boolean value) {
        return value ? True : False;
    }

    public static PyBool from(Object value) throws JyPyException {
        if (value == null || value == Py.None) {
            return from(false);
        } else if (value instanceof Boolean) {
            return from((boolean) value);
        } else if (value instanceof PyBool) {
            return from(((PyBool) value).toBoolean());
        } else if (value instanceof Integer) {
            return from(((int) value) != 0);
        } else if (value instanceof Long) {
            return from(((long) value) != 0);
        } else if (value instanceof Short) {
            return from(((short) value) != 0);
        } else if (value instanceof Byte) {
            return from(((byte) value) != 0);
        } else if (value instanceof Character || value == Py.Ellipsis || value == Py.NotImplemented) {
            return from(true); // A character represents a single-character string in Python, which always has length 1 and is thus true
        } else if (value instanceof Float) {
            return from(((Float) value) != 0);
        } else if (value instanceof Double) {
            return from(((Double) value) != 0);
        } else if (value instanceof byte[]) {
            return from((((byte[]) value).length) != 0);
        } else if (value instanceof short[]) {
            return from((((short[]) value).length) != 0);
        } else if (value instanceof char[]) {
            return from((((char[]) value).length) != 0);
        } else if (value instanceof int[]) {
            return from((((int[]) value).length) != 0);
        } else if (value instanceof long[]) {
            return from((((long[]) value).length) != 0);
        } else if (value instanceof float[]) {
            return from((((float[]) value).length) != 0);
        } else if (value instanceof double[]) {
            return from((((double[]) value).length) != 0);
        } else if (value instanceof Object[]) {
            return from((((Object[]) value).length) != 0);
        } else if (value instanceof String) {
            return from(!(((String) value).isEmpty()));
        } else if (value instanceof CharSequence) {
            return from((((CharSequence) value).length()) != 0);
        } else if (value instanceof Collection<?>) { // Includes List, Set
            return from(!((Collection<?>) value).isEmpty());
        } else if (value instanceof Map<?, ?>) {
            return from(!((Map<?, ?>) value).isEmpty());
        } else if (value instanceof BigInteger) {
            return from(!value.equals(BigInteger.ZERO));
        } else if (value instanceof BigDecimal) {
            return from(((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0);
        } else if (value instanceof Iterator<?>) {
            return from(((Iterator<?>) value).hasNext());
        } else if (value instanceof PyList) {
            return from(!((PyList) value).list.isEmpty());
        } else if (value instanceof PyUnicode) {
            return from(((PyUnicode) value).length != 0);
        } else if (value instanceof PyLong) {
            return from(!((PyLong) value).bigInteger.equals(BigInteger.ZERO));
        } else if (value instanceof PyFloat) {
            return from(((PyFloat) value).value != 0);
        } else if (value instanceof PyDict) {
            return from(!((PyDict) value).map.isEmpty());
        }
        return null;
    }
}
