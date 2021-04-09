package com.github.kb1000.jypy;

import java.util.*;

import com.github.kb1000.jypy.annotations.NotInPython;
import com.github.kb1000.jypy.annotations.VoidToNone;

public class PyList extends PyObject {
    /**
     * You may modify this {@link List}, but you may not replace it.
     */
    @NotInPython(pyObject = true)
    public final List<Object> list;

    public PyList() {
        this.list = new ArrayList<>();
    }

    public PyList(Iterable<?> iterable) {
        if (iterable instanceof Collection<?>) {
            this.list = new ArrayList<>((Collection<?>) iterable);
        } else {
            List<Object> list = new ArrayList<>();
            iterable.forEach(list::add);
            this.list = list;
        }
    }

    public PyList(Object... objects) {
        this.list = new ArrayList<>(Arrays.asList(objects));
    }

    public PyList(PyObject iterable) {
        throw new UnsupportedOperationException("Iterables not implemented"); // FIXME(kb1000)
    }

    @VoidToNone
    public void append(Object value) {
        list.add(value);
    }

    @VoidToNone
    public void extend(PyObject other) {
        //list.addAll(other); // FIXME(kb1000)
    }
}
