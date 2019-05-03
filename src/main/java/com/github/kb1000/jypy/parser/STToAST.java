package com.github.kb1000.jypy.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.tree.ParseTree;

import com.github.kb1000.jypy.compiler.CompilerState;
import com.github.kb1000.jypy.parser.antlr.Python3Parser;
import com.github.kb1000.jypy.parser.ast.Expr;
import com.github.kb1000.jypy.parser.ast.IfExp;
import com.github.kb1000.jypy.parser.ast.Load;
import com.github.kb1000.jypy.parser.ast.Module;
import com.github.kb1000.jypy.parser.ast.Pass;
import com.github.kb1000.jypy.parser.ast.Starred;
import com.github.kb1000.jypy.parser.ast.Tuple;
import com.github.kb1000.jypy.parser.ast.expr;
import com.github.kb1000.jypy.parser.ast.stmt;

public final class STToAST {
    private STToAST() {
    }

    public static Module process(final CompilerState state, Python3Parser.File_inputContext st) {
        LinkedList<stmt> statements = new LinkedList<>();
        st.stmt().stream().flatMap(arg -> process(state, arg)).forEach(statements::add);
        return new Module(statements);
    }

    public static Stream<stmt> process(final CompilerState state, Python3Parser.StmtContext st) {
        Python3Parser.Simple_stmtContext simple_stmt = st.simple_stmt();
        if (simple_stmt != null) {
            return process(state, simple_stmt);
        }

        Python3Parser.Compound_stmtContext compound_stmt = st.compound_stmt();
        if (compound_stmt != null) {
            return process(state, compound_stmt);
        }

        throw new IllegalArgumentException("Broken syntax tree, update STToAST class");
    }

    public static Stream<stmt> process(final CompilerState state, Python3Parser.Simple_stmtContext st) {
        return st.small_stmt().stream().map((arg) -> process(state, arg));
    }

    public static Stream<stmt> process(final CompilerState state, Python3Parser.Compound_stmtContext st) {
        return Stream.of((stmt) null); // FIXME(kb1000)
    }

    public static stmt process(final CompilerState state, Python3Parser.Small_stmtContext st) {
        Python3Parser.Expr_stmtContext expr_stmt = st.expr_stmt();
        if (expr_stmt != null) {
            return process(state, expr_stmt);
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

    public static stmt process(final CompilerState state, Python3Parser.Expr_stmtContext st) {
        if (st.testlist_star_expr().size() == 1 && st.testlist() == null) {
            return new Expr(process(state, st.testlist_star_expr().get(0)));
        }
        return null; // FIXME(kb1000)
    }

    public static expr process(final CompilerState state, Python3Parser.Testlist_star_exprContext st) {
        int childCount = st.getChildCount();
        if (childCount == 1) {
            ParseTree child = st.getChild(0);
	    if (child instanceof Python3Parser.TestContext) {
                return process(state, (Python3Parser.TestContext) child);
	    } else { // has to be star_expr then
                return process(state, (Python3Parser.Star_exprContext) child);
            }
        } else {
            List<expr> exprs = new LinkedList<>();
            for (int i = 0; i < childCount; i += 2) {
                ParseTree child = st.getChild(i);
                expr e;
                if (child instanceof Python3Parser.TestContext) {
                    e = process(state, (Python3Parser.TestContext) child);
                } else { // has to be star_expr otherwise
                    e = process(state, (Python3Parser.Star_exprContext) child);
                }
                exprs.add(e);
            }
            return new Tuple(exprs, Load.INSTANCE);
        }
    }

    public static Starred process(final CompilerState state, Python3Parser.Star_exprContext st) {
        return new Starred(process(state, st.expr()), Load.INSTANCE);
    }

    public static expr process(final CompilerState state, Python3Parser.TestContext st) {
        List<Python3Parser.Or_testContext> orTests = st.or_test();
        switch (orTests.size()) {
        case 1:
            return process(state, orTests.get(0));
        case 2:
            // if..else expression
            return new IfExp(process(state, orTests.get(1)), process(state, orTests.get(0)), process(state, st.test()));
        case 0:
            return process(state, st.lambdef());
        default:
            throw new IllegalArgumentException("Broken syntax tree, update STToAST class");
        }
    }

    public static expr process(final CompilerState state, Python3Parser.ExprContext st) {
        return null; // FIXME(kb1000)
    }

    public static expr process(final CompilerState state, Python3Parser.Or_testContext st) {
        return null; // FIXME(kb1000)
    }

    public static expr process(final CompilerState state, Python3Parser.LambdefContext st) {
        return null; // FIXME(kb1000)
    }
}
