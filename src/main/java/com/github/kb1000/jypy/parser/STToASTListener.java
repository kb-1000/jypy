package com.github.kb1000.jypy.parser;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

import com.github.kb1000.jypy.parser.antlr.*;
import com.github.kb1000.jypy.parser.ast.AST;
import com.github.kb1000.jypy.parser.ast.Module;
import com.github.kb1000.jypy.parser.ast.mod;
import com.github.kb1000.jypy.parser.ast.stmt;

public class STToASTListener extends Python3BaseListener {
    private ArrayDeque<List<AST>> state;
    private mod result;

    @Override
    public void enterFile_input(Python3Parser.File_inputContext st) {
        state.addFirst(new LinkedList<AST>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exitFile_input(Python3Parser.File_inputContext st) {
        // javac interestingly errors without the cast to List<?>
        result = new Module((List<stmt>)(List<?>) state.removeFirst());
    }

    @SuppressWarnings("unchecked") // It IS checked
    public <T extends mod> T getResult(Class<T> clazz) {
        assert state.isEmpty();
        if (clazz.isInstance(result)) {
            return (T) result;
        }
        throw new IllegalArgumentException(); // FIXME(kb1000): add message
    }
}
