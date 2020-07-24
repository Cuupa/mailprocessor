package com.cuupa.mailprocessor.services.archive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalArchiver implements FileProtocol {

    @Override
    public void init(String username, String password) {
        // Not implemented
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    @Override
    public boolean save(String path, byte[] data) {
        try {
            Files.copy(new ByteArrayInputStream(data), Paths.get(path));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean createDirectory(String path) {
        try {
            Files.createDirectory(Paths.get(path));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public List<ArchiveResource> list(String path) {
        try {
            return Files.list(Paths.get(path))
                        .map(e -> new ArchiveResource(e.getFileName().toString(), getContentType(e)))
                        .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private String getContentType(Path e) {
        try {
            return Files.probeContentType(e);
        } catch (IOException ioException) {
            return "*/*";
        }
    }

    @Override
    public void close() {
        // Not implemented
    }
}
