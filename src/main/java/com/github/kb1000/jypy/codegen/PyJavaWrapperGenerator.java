package com.github.kb1000.jypy.codegen;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.github.kb1000.jypy.JyPyException;

public final class PyJavaWrapperGenerator {
    private PyJavaWrapperGenerator() {
        throw new UnsupportedOperationException();
    }

    private final HashMap<Class<?>[], Class<?>> cache = new HashMap<>(); // FIXME(kb1000): class leak, not preventable by WeakHashMap though

    public final Class<?> getClassForSuperclasses(Class<?>[] classes) throws JyPyException {
        Class<?> superclass = null;
        for (Class<?> clazz: classes) {
            if (superclass == null && (!clazz.isInterface())) {
                superclass = clazz;
            }
            else if ((!clazz.isInterface()) /* implies superclass is not null */) {
                if (superclass.isAssignableFrom(clazz)) {
                    superclass = clazz;
                } else {
                    throw new JyPyException(); // new PyTypeError("Error when calling the metaclass bases\nno multiple inheritance for Java classes: " + clazz.getName() + " and " + superclass.getName()) // FIXME(kb1000): add Jython-like exception
                }
            }
        }
        if (Modifier.isFinal(superclass.getModifiers())) {
            throw new JyPyException(); // FIXME(kb1000): add Jython-like exception
        }

        return null; // FIXME(kb1000)
    }
}
