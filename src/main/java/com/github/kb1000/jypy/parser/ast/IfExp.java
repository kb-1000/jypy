package com.github.kb1000.jypy.parser.ast;

public class IfExp extends expr {
    public expr test;
    public expr body;
    public expr orelse;

    public IfExp(expr test, expr body, expr orelse) {
        this.test = test;
        this.body = body;
        this.orelse = orelse;
    }
}
