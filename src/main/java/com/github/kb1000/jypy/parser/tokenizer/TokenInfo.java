package com.github.kb1000.jypy.parser.tokenizer;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TokenInfo {
    private final int type;
    private final String string;
    private final Position start;
    private final Position end;
    private final String line;

    public TokenInfo(int type, String string, Position start, Position end, String line) {
        this.type = type;
        this.string = string;
        this.start = start;
        this.end = end;
        this.line = line;
    }

    public int getType() {
        return type;
    }

    public String getString() {
        return string;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public String getLine() {
        return line;
    }

    public static final class Position {
        private final int line;
        private final int column;

        public Position(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

        public static final class Serializer extends TypeAdapter<Position> {
            public static final Serializer INSTANCE = new Serializer();

            @Contract(value = "_, _ -> fail", pure = true)
            @Override
            public void write(JsonWriter out, Position value) {
                throw new UnsupportedOperationException("serialization not supported");
            }

            @Contract("_ -> new")
            @Override
            public @NotNull Position read(JsonReader in) throws IOException {
                in.beginArray();
                int line = in.nextInt();
                int column = in.nextInt();
                in.endArray();
                return new Position(line, column);
            }
        }
    }

    public static final class Serializer extends TypeAdapter<TokenInfo> {
        public static final Serializer INSTANCE = new Serializer();

        @Contract(value = "_, _ -> fail", pure = true)
        @Override
        public void write(JsonWriter out, TokenInfo value) {
            throw new UnsupportedOperationException("serialization not supported");
        }

        @Override
        public TokenInfo read(JsonReader in) throws IOException {
            in.beginArray();
            int type = in.nextInt();
            String string = in.nextString();
            Position start = Position.Serializer.INSTANCE.read(in);
            Position end = Position.Serializer.INSTANCE.read(in);
            String line = in.nextString();
            in.endArray();
            return new TokenInfo(type, string, start, end, line);
        }
    }
}
