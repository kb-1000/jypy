package com.github.kb1000.jypy.common;

public final class Reference <T> {
    public T value;

    public Reference(T value) {
        this.value = value;
    }

    public Reference() {
        this.value = null;
    }
}
