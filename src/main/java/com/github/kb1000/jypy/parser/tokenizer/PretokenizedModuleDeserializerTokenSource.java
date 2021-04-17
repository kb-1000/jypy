package com.github.kb1000.jypy.parser.tokenizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenFactory;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PretokenizedModuleDeserializerTokenSource implements TokenSource {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(TokenInfo.class, TokenInfo.Serializer.INSTANCE)
            .registerTypeAdapter(TokenInfo.Position.class, TokenInfo.Position.Serializer.INSTANCE)
            .create();

    private TokenFactory<?> tokenFactory = CommonTokenFactory.DEFAULT;
    private final String sourceName;
    private final Iterator<TokenInfo> tokenIterator;
    private Token lastToken;
    private TokenInfo lastTokenInfo;
    private final CharStream charStream;

    public PretokenizedModuleDeserializerTokenSource(String sourceName) throws IOException {
        this.sourceName = sourceName;
        List<TokenInfo> tokens;
        try (InputStream inputStream = PretokenizedModuleDeserializerTokenSource.class.getResourceAsStream('/' + sourceName + ".json")) {
            if (inputStream != null) {
                try (
                        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                        BufferedReader bufferedReader = new BufferedReader(reader)
                ) {
                    tokens = GSON.fromJson(bufferedReader, new TypeToken<List<TokenInfo>>() {}.getType());
                }
            } else {
                throw new FileNotFoundException(sourceName + ".json");
            }
        }
        tokenIterator = tokens.iterator();
        charStream = CharStreams.fromStream(PretokenizedModuleDeserializerTokenSource.class.getResourceAsStream('/' + sourceName), StandardCharsets.UTF_8);
    }

    @Override
    public Token nextToken() {
        if (tokenIterator.hasNext()) {
            final TokenInfo token = tokenIterator.next();
            this.lastTokenInfo = token;
            return lastToken = tokenFactory.create(new Pair<>(this, charStream), token.getType(), token.getString(), Token.DEFAULT_CHANNEL, -1, -1, token.getStart().getLine(), token.getStart().getColumn());
        } else {
            return lastToken;
        }
    }

    @Override
    public int getLine() {
        if (lastTokenInfo == null)
            return 1;
        return lastTokenInfo.getStart().getLine();
    }

    @Override
    public int getCharPositionInLine() {
        if (lastTokenInfo == null)
            return 0;
        return lastTokenInfo.getStart().getColumn();
    }

    @Override
    public CharStream getInputStream() {
        return charStream;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public void setTokenFactory(TokenFactory<?> tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public TokenFactory<?> getTokenFactory() {
        return tokenFactory;
    }
}
