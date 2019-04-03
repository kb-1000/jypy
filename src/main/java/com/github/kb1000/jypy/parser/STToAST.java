package com.github.kb1000.jypy.parser;

import java.util.LinkedList;
import java.util.stream.Stream;

import com.github.kb1000.jypy.parser.antlr.Python3Parser;
import com.github.kb1000.jypy.parser.ast.Module;
import com.github.kb1000.jypy.parser.ast.Pass;
import com.github.kb1000.jypy.parser.ast.stmt;

public final class STToAST {
    private STToAST() {
    }

    public static Module process(Python3Parser.File_inputContext st) {
        LinkedList<stmt> statements = new LinkedList<>();
        st.stmt().stream().flatMap(STToAST::process).forEach(statements::add);
        return new Module(statements);
    }

    public static Stream<stmt> process(Python3Parser.StmtContext st) {
        Python3Parser.Simple_stmtContext simple_stmt = st.simple_stmt();
        if (simple_stmt != null) {
            return process(simple_stmt);
        }

        Python3Parser.Compound_stmtContext compound_stmt = st.compound_stmt();
        if (compound_stmt != null) {
            return process(compound_stmt);
        }

        throw new IllegalArgumentException("Broken syntax tree, update STToAST class");
    }

    public static Stream<stmt> process(Python3Parser.Simple_stmtContext st) {
        return st.small_stmt().stream().map(STToAST::process);
    }

    public static Stream<stmt> process(Python3Parser.Compound_stmtContext st) {
        return Stream.of((stmt) null); // FIXME(kb1000)
    }

    public static stmt process(Python3Parser.Small_stmtContext st) {
        Python3Parser.Expr_stmtContext expr_stmt = st.expr_stmt();
        if (expr_stmt != null) {
            return null; // FIXME(kb1000)
        }

        Python3Parser.Del_stmtContext del_stmt = st.del_stmt();
        if (del_stmt != null) {
            return null; // FIXME(kb1000)
        }

        if (st.pass_stmt() != null) {
            return new Pass();
        }

        return null; // FIXME(kb1000)
    }
}
