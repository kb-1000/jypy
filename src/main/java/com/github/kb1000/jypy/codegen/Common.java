package com.github.kb1000.jypy.codegen;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class Common {
    static ClassLoader loader;

    /**
     * @throws IllegalStateException if the class loader for code generation has already been created
     */
    public synchronized static void initializeParentClassLoader(ClassLoader parent) {
        if (loader == null) {
            loader = AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> new ClassLoader(parent) {}, null, new RuntimePermission("createClassLoader"));
        } else {
            throw new IllegalStateException("The code generation class loader is already initialized, cannot replace its parent!");
        }
    }
}
