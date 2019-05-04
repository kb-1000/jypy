package com.github.kb1000.jypy.parser.ast;

public class BinOp extends expr {
    public expr left;
    public operator op;
    public expr right;

    public BinOp(expr left, operator op, expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}
