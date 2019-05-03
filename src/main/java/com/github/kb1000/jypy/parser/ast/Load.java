package com.github.kb1000.jypy.parser.ast;

public class Load extends expr_context {
    public static final Load INSTANCE = new Load();

    /**
     * Prefer {@link #INSTANCE} if possible.
     */
    public Load() {
    }
}
