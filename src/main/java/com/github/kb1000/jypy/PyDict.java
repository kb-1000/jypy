package com.github.kb1000.jypy;

import java.util.HashMap;
import java.util.Map;

public class PyDict extends PyObject {
    public final Map<PyObject, PyObject> map = new HashMap<>();
}
