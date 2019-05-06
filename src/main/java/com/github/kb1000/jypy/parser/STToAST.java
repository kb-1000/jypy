package com.github.kb1000.jypy.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.kb1000.jypy.compiler.CompilerState;
import com.github.kb1000.jypy.parser.antlr.Python3Lexer;
import com.github.kb1000.jypy.parser.antlr.Python3Parser;
import com.github.kb1000.jypy.parser.ast.*;
import com.github.kb1000.jypy.parser.ast.Module;

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
        List<Python3Parser.Xor_exprContext> xor_exprs = st.xor_expr();
        Iterator<Python3Parser.Xor_exprContext> iterator = xor_exprs.iterator();
        expr expression = process(state, iterator.next());
        while (iterator.hasNext()) {
            expression = new BinOp(expression, BitOr.INSTANCE, process(state, iterator.next()));
        }
        return expression;
    }

    public static expr process(final CompilerState state, Python3Parser.Or_testContext st) {
        return null; // FIXME(kb1000)
    }

    public static expr process(final CompilerState state, Python3Parser.LambdefContext st) {
        return null; // FIXME(kb1000)
    }

    public static expr process(final CompilerState state, Python3Parser.Xor_exprContext st) {
        Iterator<Python3Parser.And_exprContext> iterator = st.and_expr().iterator();
        expr expression = process(state, iterator.next());
        while (iterator.hasNext()) {
            expression = new BinOp(expression, BitXor.INSTANCE, process(state, iterator.next()));
        }
        return expression;
    }

    public static expr process(final CompilerState state, Python3Parser.And_exprContext st) {
        Iterator<Python3Parser.Shift_exprContext> iterator = st.shift_expr().iterator();
        expr expression = process(state, iterator.next());
        while (iterator.hasNext()) {
            expression = new BinOp(expression, BitAnd.INSTANCE, process(state, iterator.next()));
        }
        return expression;
    }

    public static expr process(final CompilerState state, Python3Parser.Shift_exprContext st) {
        expr expression = process(state, (Python3Parser.Arith_exprContext) st.getChild(0));
        final int childCount = st.getChildCount();
        for (int i = 1; i < childCount; i++) {
            switch (((TerminalNode) st.getChild(i++)).getSymbol().getType()) {
            case Python3Lexer.LEFT_SHIFT:
                expression = new BinOp(expression, LShift.INSTANCE, process(state, (Python3Parser.Arith_exprContext) st.getChild(i)));
                break;
	    case Python3Lexer.RIGHT_SHIFT:
                expression = new BinOp(expression, RShift.INSTANCE, process(state, (Python3Parser.Arith_exprContext) st.getChild(i)));
                break;
            default:
                throw new IllegalArgumentException("Broken syntax tree, update STToAST class");
            }
        }
        return expression;
    }

    public static expr process(final CompilerState state, Python3Parser.Arith_exprContext st) {
        expr expression = process(state, (Python3Parser.TermContext) st.getChild(0));
        final int childCount = st.getChildCount();
        for (int i = 1; i < childCount; i++) {
            switch (((TerminalNode) st.getChild(i++)).getSymbol().getType()) {
            case Python3Lexer.ADD:
                expression = new BinOp(expression, Add.INSTANCE, process(state, (Python3Parser.TermContext) st.getChild(i)));
                break;
	    case Python3Lexer.MINUS:
                expression = new BinOp(expression, Sub.INSTANCE, process(state, (Python3Parser.TermContext) st.getChild(i)));
                break;
            default:
                throw new IllegalArgumentException("Broken syntax tree, update STToAST class");
            }
        }
        return expression;
    }

    public static expr process(final CompilerState state, Python3Parser.TermContext st) {
        expr expression = process(state, (Python3Parser.FactorContext) st.getChild(0));
        final int childCount = st.getChildCount();
        for (int i = 1; i < childCount; i++) {
            switch (((TerminalNode) st.getChild(i++)).getSymbol().getType()) {
            case Python3Lexer.STAR:
                expression = new BinOp(expression, Mult.INSTANCE, process(state, (Python3Parser.FactorContext) st.getChild(i)));
                break;
	    case Python3Lexer.AT:
                expression = new BinOp(expression, MatMult.INSTANCE, process(state, (Python3Parser.FactorContext) st.getChild(i)));
                break;
	    case Python3Lexer.DIV:
                expression = new BinOp(expression, Div.INSTANCE, process(state, (Python3Parser.FactorContext) st.getChild(i)));
                break;
	    case Python3Lexer.MOD:
                expression = new BinOp(expression, Mod.INSTANCE, process(state, (Python3Parser.FactorContext) st.getChild(i)));
                break;
	    case Python3Lexer.IDIV:
                expression = new BinOp(expression, FloorDiv.INSTANCE, process(state, (Python3Parser.FactorContext) st.getChild(i)));
                break;
            default:
                throw new IllegalArgumentException("Broken syntax tree, update STToAST class");
            }
        }
        return expression;
    }

    public static expr process(final CompilerState state, Python3Parser.FactorContext st) {
        return null; // FIXME(kb1000)
    }
}
