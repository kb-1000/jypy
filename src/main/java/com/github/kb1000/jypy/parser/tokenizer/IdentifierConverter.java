package com.github.kb1000.jypy.parser.tokenizer;

import static com.github.kb1000.jypy.parser.antlr.Python3.*;

public class IdentifierConverter {
    public static int convertKeyword(String text) {
        switch (text) {
        case "def":
            return DEF;
        case "del":
            return DEL;
        case "pass":
            return PASS;
        case "break":
            return BREAK;
        case "continue":
            return CONTINUE;
        case "return":
            return RETURN;
        case "raise":
            return RAISE;
        case "from":
            return FROM;
        case "import":
            return IMPORT;
        case "as":
            return AS;
        case "global":
            return GLOBAL;
        case "nonlocal":
            return NONLOCAL;
        case "assert":
            return ASSERT;
        case "if":
            return IF;
        case "elif":
            return ELIF;
        case "else":
            return ELSE;
        case "while":
            return WHILE;
        case "for":
            return FOR;
        case "in":
            return IN;
        case "try":
            return TRY;
        case "finally":
            return FINALLY;
        case "with":
            return WITH;
        case "except":
            return EXCEPT;
        case "lambda":
            return LAMBDA;
        case "or":
            return OR;
        case "and":
            return AND;
        case "not":
            return NOT;
        case "is":
            return IS;
        case "None":
            return NONE;
        case "True":
            return TRUE;
        case "False":
            return FALSE;
        case "class":
            return CLASS;
        case "yield":
            return YIELD;
        default:
            return NAME;
        }
    }
}