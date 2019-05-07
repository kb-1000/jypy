package com.github.kb1000.jypy.parser.ast;

public class UAdd extends unaryop {
    public static final UAdd INSTANCE = new UAdd();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public UAdd() {
    }
}
