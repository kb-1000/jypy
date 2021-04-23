package com.github.kb1000.jypy;

import java.io.IOException;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;

import com.github.kb1000.jypy.compiler.CompilerState;
import com.github.kb1000.jypy.parser.STToAST;
import com.github.kb1000.jypy.parser.antlr.Python3;
import com.github.kb1000.jypy.parser.ast.Module;
import com.github.kb1000.jypy.parser.tokenizer.PretokenizedModuleDeserializerTokenSource;

public final class Main {
    private Main() {
    }

    public static void main(String... args) throws IOException {
        final Python3 python3Parser = new Python3(new CommonTokenStream(new PretokenizedModuleDeserializerTokenSource("tokenize.py")));
        python3Parser.setErrorHandler(new BailErrorStrategy());
        final Python3.File_inputContext file_inputContext = python3Parser.file_input();
        final Module tree = STToAST.process(new CompilerState(), file_inputContext);
        System.out.println(tree);
    }
}
