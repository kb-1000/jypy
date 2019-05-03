package com.github.kb1000.jypy.parser.ast;

import java.util.List;

public class Tuple extends expr {
    public List<expr> elts;
    public expr_context ctx;

    public Tuple(List<expr> elts, expr_context ctx) {
        this.elts = elts;
        this.ctx = ctx;
    }
}
