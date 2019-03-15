package com.github.kb1000.jypy;

import java.util.ArrayList;
import java.util.List;

import com.github.kb1000.jypy.annotations.NotInPython;
import com.github.kb1000.jypy.annotations.VoidToNone;

public class PyList extends PyObject {
    /**
     * You may modify this {@link List}, but you may not replace it.
     */
    @NotInPython(pyObject = true)
    public final List<PyObject> list;

    public PyList() {
        this.list = Collections.checkedList(new ArrayList<PyObject>(), PyObject.class);
    }

    public PyList(Iterable<? extends PyObject> iterable) {
        this.list = Collections.checkedList(new ArrayList<PyObject>(iterable), PyObject.class);
    }

    public PyList(PyObject... objects) {
        this.list = Collections.checkedList(new ArrayList<PyObject>(objects), PyObject.class);
    }

    public PyList(PyObject iterable) {
        throw new UnsupportedOperationException("Iterables not implemented"); // FIXME(kb1000)
    }

    @VoidToNone
    public void append(PyObject value) {
        list.add(value);
    }

    @VoidToNone
    public void extend(PyObject other) {
        list.addAll(other);
    }
}
