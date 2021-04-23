package com.github.kb1000.jypy.parser.ast;

public class Not extends unaryop {
    public static final Not INSTANCE = new Not();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public Not() {
    }
}
