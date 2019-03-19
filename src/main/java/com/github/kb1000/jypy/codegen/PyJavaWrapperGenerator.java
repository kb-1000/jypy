package com.github.kb1000.jypy.codegen;

import com.github.kb1000.jypy.PyJavaWrapperObject;
import com.github.kb1000.jypy.common.ClassMap;
import org.objectweb.asm.ClassWriter;

public class PyJavaWrapperGenerator {
    private PyJavaWrapperGenerator() {
    }

    private static final ClassMap<Class<? extends PyJavaWrapperObject>> cache = new ClassMap<>() {
        @Override
        protected Class<? extends PyJavaWrapperObject> computeValue(Class<?> clazz) {
            return makeWrapperClass(clazz);
        }
    };

    public static Class<? extends PyJavaWrapperObject> getWrapperClass(Class<?> clazz) {
        return null;
    }

    private static byte[] generateWrapperClass(Class<?> clazz) {
        return null;
    }

    private static Class<? extends PyJavaWrapperObject> makeWrapperClass(Class<?> clazz) {
        return null;
    }
}
