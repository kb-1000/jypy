package com.github.kb1000.jypy.parser.ast;

import java.util.List;

public final class Module extends mod {
    public List<stmt> body;

    public Module(List<stmt> body) {
        this.body = body;
    }
}
