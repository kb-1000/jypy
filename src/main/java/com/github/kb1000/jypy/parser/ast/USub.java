package com.github.kb1000.jypy.parser.ast;

public class USub extends unaryop {
    public static final USub INSTANCE = new USub();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public USub() {
    }
}
