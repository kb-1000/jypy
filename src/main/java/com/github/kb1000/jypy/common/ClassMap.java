package com.github.kb1000.jypy.common;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.WeakHashMap;

public abstract class ClassMap<T> {
    private final WeakHashMap<Class<?>, Reference<T>> weakMap = new WeakHashMap<>();

    protected abstract T computeValue(Class<?> clazz);

    public final T get(Class<?> clazz) {
        Reference<T> ref = weakMap.get(clazz);
        if (ref != null) {
            T value = ref.get();
            if (value != null) {
                return value;
            }
        }
        T value = computeValue(clazz);
        weakMap.putIfAbsent(clazz, new SoftReference<T>(value));
        return value;
    }
}
