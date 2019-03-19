package com.github.kb1000.jypy;

import java.util.HashMap;
import java.util.Map;

public class PyDict extends PyObject {
    public final Map<PyObject, Object> map = new HashMap<>();
}
