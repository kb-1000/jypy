package com.github.kb1000.jypy.parser.ast;

import java.util.List;

public class BoolOp extends expr {
    public boolop op;
    public List<expr> values;

    public BoolOp(boolop op, List<expr> values) {
        this.op = op;
        this.values = values;
    }
}
