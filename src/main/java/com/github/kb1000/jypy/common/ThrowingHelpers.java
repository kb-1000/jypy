package com.github.kb1000.jypy.common;

import java.util.function.*;

public final class ThrowingHelpers {
    @FunctionalInterface
    public interface ThrowingFunction<T, R> {
        public R apply(T t) throws Exception;
    }

    public static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> checked) {
        return t -> {
            try {
                return checked.apply(t);
            } catch (Exception e) {
                return sneakyThrow(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable, R> R sneakyThrow(Throwable throwable) throws T {
        throw (T) throwable;
    }
}
