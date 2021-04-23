package com.github.kb1000.jypy.parser.ast;

public class And extends boolop {
    public static final And INSTANCE = new And();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public And() {
    }
}
