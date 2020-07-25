package com.cuupa.mailprocessor.userconfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ScanProperties extends ConfigurationProperties {

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("path")
    @Expose
    private int port;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("scannerprefix")
    @Expose
    private List<String> scannerPrefix;

    @SerializedName("filetypes")
    @Expose
    private List<String> fileTypes;

    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public ScanProperties path(final String path) {
        this.path = path;
        return this;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public ScanProperties port(int port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public ScanProperties username(final String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public ScanProperties password(final String password) {
        this.password = password;
        return this;
    }

    public List<String> getScannerPrefix() {
        return scannerPrefix != null ? scannerPrefix : new ArrayList<>();
    }

    public void setScannerPrefix(final List<String> scannerPrefix) {
        this.scannerPrefix = scannerPrefix;
    }

    public ScanProperties scannerPrefix(final List<String> scannerPrefix) {
        this.scannerPrefix = scannerPrefix;
        return this;
    }

    public List<String> getFileTypes() {
        return fileTypes != null ? fileTypes : new ArrayList<>();
    }

    public void setFileTypes(final List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public ScanProperties fileTypes(final List<String> fileTypes) {
        this.fileTypes = fileTypes;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public ScanProperties enabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("path", path)
                                        .append("port", port)
                                        .append("username", username)
                                        .append("password", getPasswordStared(password))
                                        .append("scannerprefix", scannerPrefix)
                                        .append("filetypes", fileTypes)
                                        .append("enabled", enabled)
                                        .toString();
    }
}
