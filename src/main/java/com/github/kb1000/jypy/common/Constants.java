package com.github.kb1000.jypy.common;

import java.util.Objects;
import java.util.function.*;

public final class Constants {
    private Constants() {
    }

    public static final Predicate<?> nonNullPredicate = Objects::nonNull;
    public static final IntFunction<Class<?>[]> newClassArray = Class<?>[]::new;

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> nonNullPredicate() {
        return (Predicate<T>) nonNullPredicate;
    }
}
