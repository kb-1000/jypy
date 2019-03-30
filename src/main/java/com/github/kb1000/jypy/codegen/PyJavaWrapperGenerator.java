package com.github.kb1000.jypy.codegen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Stream;

import com.github.kb1000.jypy.JyPyException;
import com.github.kb1000.jypy.PyObject;
import com.github.kb1000.jypy.common.Constants;
import com.github.kb1000.jypy.common.ThrowingHelpers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class PyJavaWrapperGenerator {
    private PyJavaWrapperGenerator() {
        throw new UnsupportedOperationException();
    }

    private static final HashMap<Class<?>[], Class<?>> cache = new HashMap<>(); // FIXME(kb1000): class leak, not preventable by WeakHashMap though

    public static Class<?> getClassForSuperclasses(Class<?>... classes) throws JyPyException {
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

        superclass = Optional.<Class<?>>ofNullable(superclass).orElse(Object.class);

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
        Arrays.sort(newClasses, 1, newClasses.length, (c1, c2) -> c1.getName().compareTo(c2.getName()));

        synchronized (cache) {
            return cache.computeIfAbsent(newClasses, ThrowingHelpers.unchecked(PyJavaWrapperGenerator::makeClass));
        }
    }

    private static Class<?> makeClass(Class<?>[] classes) throws JyPyException {
        String name = getClassName(classes);
        try {
            return Common.loadClass(name);
        } catch (ClassNotFoundException cfne) {
        }
        return Common.defineClass(generateClass(name.replace('.', '/'), classes), classes); // FIXME(kb1000): other classes might be referenced in method signatures
    }

    private static String getClassName(Class<?>[] classes) {
        StringJoiner joiner = new StringJoiner("_", PyJavaWrapperGenerator.class.getPackage().getName() + ".generated.javawrapper.", "");
        for (final Class<?> clazz: classes) {
            joiner.add(Common.getBinaryName(clazz).replace(';', '_').replace('/', '.'));
        }
        return joiner.toString();
    }

    private static byte[] generateClass(String name, Class<?>[] classes) {
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        String superTypeName = Type.getInternalName(classes[0]);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, name, null, superTypeName, Arrays.stream(classes).skip(1).map(Type::getDescriptor).toArray(Constants.newStringArray));
        cw.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "$jypyWrappedObject", Type.getDescriptor(PyObject.class), null, null);
        for (final Constructor<?> constructor: classes[0].getDeclaredConstructors()) {
            final MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.getType(void.class), Stream.of(Stream.of(PyObject.class), Arrays.stream(constructor.getParameterTypes())).flatMap(stream -> stream).map(Type::getType).toArray(Type[]::new)), null, null);
            mw.visitParameter("$wrappedPyObject", Opcodes.ACC_FINAL);
            for (final Parameter parameter: constructor.getParameters()) {
                mw.visitParameter(parameter.getName(), Opcodes.ACC_FINAL);
            }
            mw.visitCode();
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitVarInsn(Opcodes.ALOAD, 1);
            mw.visitFieldInsn(Opcodes.PUTFIELD, name, "$jypyWrappedObject", Type.getDescriptor(PyObject.class));

            mw.visitVarInsn(Opcodes.ALOAD, 0);
            int i = 2;
            for (Class<?> type: constructor.getParameterTypes()) {
                mw.visitVarInsn(Type.getType(type).getOpcode(Opcodes.ILOAD), i);
                i++;
                if (type == long.class || type == double.class) {
                    i++;
                }
            }
            mw.visitMethodInsn(Opcodes.INVOKESPECIAL, superTypeName, "<init>", Type.getConstructorDescriptor(constructor), false);

            mw.visitInsn(Opcodes.RETURN);
            mw.visitMaxs(-1, -1);
            mw.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}
