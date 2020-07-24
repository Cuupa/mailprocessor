package com.cuupa.mailprocessor.services.archive;

import java.util.List;

public interface FileProtocol extends AutoCloseable {

    void init(String username, String password);

    boolean exists(String path);

    boolean save(String path, byte[] data);

    boolean createDirectory(String path);

    List<ArchiveResource> list(String path);
}
