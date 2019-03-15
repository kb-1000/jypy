package com.github.kb1000.jypy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
        if (iterable instanceof Collection<?>) {
            this.list = Collections.checkedList(new ArrayList<PyObject>((Collection<? extends PyObject>) iterable), PyObject.class);
        } else {
            List<PyObject> list = Collections.checkedList(new ArrayList<PyObject>(), PyObject.class);
            iterable.forEach(list::add);
            this.list = list;
        }
    }

    public PyList(PyObject... objects) {
        this.list = Collections.checkedList(new ArrayList<PyObject>(Arrays.asList(objects)), PyObject.class);
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
        //list.addAll(other); // FIXME(kb1000)
    }
}
