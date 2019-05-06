package com.github.kb1000.jypy.parser.ast;

public class MatMult extends operator {
    public static final MatMult INSTANCE = new MatMult();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public MatMult() {
    }
}
