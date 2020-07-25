package com.cuupa.mailprocessor.services.archive;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebDavArchiver implements FileProtocol {

    private Sardine sardine;

    @Override
    public void init(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            sardine = SardineFactory.begin();
        } else {
            sardine = SardineFactory.begin(username, password);
        }
        sardine.enableCompression();
    }

    @Override
    public boolean exists(String path) {
        try {
            return sardine.exists(path);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean save(String path, byte[] data) {
        try {
            sardine.put(path, data);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean createDirectory(String path) {
        try {
            sardine.createDirectory(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public List<ArchiveResource> list(String path) {
        try {
            return sardine.list(path, 1)
                          .stream()
                          .map(e -> new ArchiveResource(e.getName(), e.getContentType()))
                          .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void close() throws Exception {
        if (sardine != null) {
            sardine.shutdown();
        }
    }
}
