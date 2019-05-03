package com.github.kb1000.jypy.parser.ast;

public class Starred extends expr {
    public expr value;
    public expr_context ctx;

    public Starred(expr value, expr_context ctx) {
        this.value = value;
        this.ctx = ctx;
    }
}
