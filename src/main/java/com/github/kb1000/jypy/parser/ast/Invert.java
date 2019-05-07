package com.github.kb1000.jypy.parser.ast;

public class Invert extends unaryop {
    public static final Invert INSTANCE = new Invert();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public Invert() {
    }
}
