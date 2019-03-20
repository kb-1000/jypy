package com.github.kb1000.jypy.codegen;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.kb1000.jypy.JyPyException;
import com.github.kb1000.jypy.common.Constants;

public final class PyJavaWrapperGenerator {
    private PyJavaWrapperGenerator() {
        throw new UnsupportedOperationException();
    }

    private static final HashMap<Class<?>[], Class<?>> cache = new HashMap<>(); // FIXME(kb1000): class leak, not preventable by WeakHashMap though

    public static Class<?> getClassForSuperclasses(Class<?>[] classes) throws JyPyException {
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
        if (superclass != null && Modifier.isFinal(superclass.getModifiers())) {
            throw new JyPyException(); // FIXME(kb1000): add Jython-like exception
        }

        @SuppressWarnings("unchecked") // Why the hell is this even then unchecked when creating an array of Stream<Class<?>> instead...
	Class<?>[] intermediateNewClasses = Arrays.stream((Stream<Class<?>>[]) new Stream<?>[] {(Stream<Class<?>>) Optional.ofNullable(superclass).stream(), Arrays.stream(classes).filter(Class::isInterface).distinct() }).flatMap(stream -> stream).toArray(Constants.newClassArray);

        {
            int len = intermediateNewClasses.length;
            for (int i = 0; i < len; i++) {
                final int i2 = i;
                if (Arrays.stream(intermediateNewClasses).filter(clazz -> clazz != null && clazz != intermediateNewClasses[i2] && intermediateNewClasses[i2].isAssignableFrom(clazz)).count() != 0) {
                    intermediateNewClasses[i] = null;
                }
            }
        }

        Class<?>[] newClasses = Arrays.stream(intermediateNewClasses).filter(Constants.nonNullPredicate()).toArray(Constants.newClassArray);

        System.out.println(Arrays.toString(newClasses));

        return null; // FIXME(kb1000)
    }
}
