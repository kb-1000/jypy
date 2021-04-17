package com.github.kb1000.jypy.common;

import java.util.function.*;

public final class ThrowingHelpers {
    @FunctionalInterface
    public interface ThrowingFunction<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    public static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> checked) {
        return t -> {
            try {
                return checked.apply(t);
            } catch (Throwable e) {
                return sneakyThrow(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable, R> R sneakyThrow(Throwable throwable) throws T {
        throw (T) throwable;
    }
}
