package com.github.kb1000.jypy.parser.ast;

public class UnaryOp extends expr {
    public unaryop op;
    public expr operand;

    public UnaryOp(unaryop op, expr operand) {
        this.op = op;
        this.operand = operand;
    }
}
