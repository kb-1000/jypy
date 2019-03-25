package com.github.kb1000.jypy.codegen;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.kb1000.jypy.common.Files2;
import com.github.kb1000.jypy.common.Reference;
import com.github.kb1000.jypy.JyPyException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

public final class Common {
    static CodegenClassLoader loader;

    static class CodegenClassLoader extends ClassLoader {
        final HashMap<String, Class<?>> classes = new HashMap<>();

        CodegenClassLoader(ClassLoader parent) {
            super(parent);
        }

        void addClass(Class<?> clazz) {
            synchronized (classes) {
                classes.put(clazz.getName(), clazz);
            }
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            synchronized (classes) {
                Class<?> clazz = classes.get(name);
                if (clazz != null) {
                    assert clazz.getName().equals(name);
                    return clazz;
                }
            }
            return super.loadClass(name);
        }

        Class<?> defineCodegenClass(byte[] data, Class<?>... referred) {
            for (final Class<?> clazz: referred) {
                addClass(clazz);
            }
            return defineClass(null, data, 0, data.length);
        }
    }

    /**
     *
     * @throws IllegalStateException When the code generation class loader isn't
     * initialized, which means {@link #initializeParentClassLoader(ClassLoader)}
     * hasn't ever been called
     */
    static Class<?> defineClass(byte[] data, Class<?>... referred) throws JyPyException {
        checkLoaderInitialized();
        if (dump == null) {
            return loader.defineCodegenClass(data, referred);
        } else {
            try {
                Files2.write(Arrays.stream((getInternalName(data) + ".class").split("/")).map(Paths::get).reduce(dump, Path::resolve, Path::resolve), data, true);
	    } catch (IOException e) {
                throw new JyPyException(e);
            }
            return loader.defineCodegenClass(data, referred);
        }
    }

    private static void checkLoaderInitialized() {
        if (loader == null) {
            throw new IllegalStateException("Code generation class loader not initialized! Parent class loader not set!");
        }
    }

    static Class<?> loadClass(String name) throws ClassNotFoundException {
        checkLoaderInitialized();
        return loader.loadClass(name);
    }

    /**
     * @throws IllegalStateException if the class loader for code generation has already been created
     */
    public synchronized static void initializeParentClassLoader(ClassLoader parent) {
        if (loader == null) {
            loader = AccessController.doPrivileged((PrivilegedAction<CodegenClassLoader>) () -> new CodegenClassLoader(parent), null, new RuntimePermission("createClassLoader"));
        } else {
            throw new IllegalStateException("The code generation class loader is already initialized, cannot replace its parent!");
        }
    }

    public static String getBinaryName(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass().getName().substring(1);
    }

    private static final Path dump;

    public static String getInternalName(byte[] data) {
        return new ClassReader(data).getClassName();
    }

    static {
        String packageName = Common.class.getPackage().getName();
        dump = Optional.ofNullable(System.getProperty(packageName + ".dump")).map(Paths::get).orElse(null);
    }
}
