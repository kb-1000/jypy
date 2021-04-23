package com.github.kb1000.jypy.parser.ast;

public class Or extends boolop {
    public static final Or INSTANCE = new Or();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public Or() {
    }
}
