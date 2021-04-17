package com.github.kb1000.jypy;

import java.io.IOException;

import org.antlr.v4.runtime.CommonTokenStream;

import com.github.kb1000.jypy.parser.antlr.Python3Parser;
import com.github.kb1000.jypy.parser.tokenizer.PretokenizedModuleDeserializerTokenSource;

public final class Main {
    private Main() {
    }

    public static void main(String... args) throws IOException {
        final PretokenizedModuleDeserializerTokenSource pretokenizedModuleDeserializerTokenSource = new PretokenizedModuleDeserializerTokenSource("tokenize.py");
        if (false) {
            System.out.println(pretokenizedModuleDeserializerTokenSource.nextToken());
        }
        final Python3Parser python3Parser = new Python3Parser(new CommonTokenStream(new PretokenizedModuleDeserializerTokenSource("tokenize.py")));
        final Python3Parser.File_inputContext file_inputContext = python3Parser.file_input();
        System.out.println(file_inputContext.toStringTree(python3Parser));
    }
}
