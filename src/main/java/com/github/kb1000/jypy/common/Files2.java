package com.github.kb1000.jypy.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Files2 {
    private Files2() {
        throw new UnsupportedOperationException("No instances for you!");
    }

    public static void write(Path path, byte[] data, boolean createDirectory) throws IOException {
        if (createDirectory) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, data);
    }
}
