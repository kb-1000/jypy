package com.github.kb1000.jypy;

import java.util.HashMap;
import java.util.Map;

public class PyDict extends PyObject {
    // FIXME(kb1000): Python dicts are now ordered. This might require a custom hashmap.
    public final Map<PyObject, Object> map = new HashMap<>();
}
