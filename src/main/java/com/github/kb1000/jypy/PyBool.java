package com.github.kb1000.jypy;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import com.github.kb1000.jypy.annotations.NotInPython;

public final class PyBool extends PyLong {
    /**
     * Do not ever use this, whether using reflection or anything else!
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
        if (value == null || value instanceof PyNone) {
            return from(false);
	} else if (value instanceof Boolean) {
            return from((boolean) value);
        } else if (value instanceof PyBool) {
            return from(((PyBool) value).toBoolean());
        } else if (value instanceof Integer) {
            return from(((Integer) value) != 0);
        } else if (value instanceof Long) {
            return from(((Long) value) != 0);
        } else if (value instanceof Short) {
            return from(((Short) value) != 0);
        } else if (value instanceof Byte) {
            return from(((Byte) value) != 0);
        } else if (value instanceof Character || value instanceof PyEllipsis || value instanceof PyNotImplemented) {
            return from(true); // A character represents a single-character string in Python, which has always length 1 and is thus true
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
        }
        return null;
    }
}
