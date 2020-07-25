package com.cuupa.mailprocessor.userconfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class ArchiveProperties extends ConfigurationProperties {

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public ArchiveProperties path(final String path) {
        this.path = path;
        return this;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public ArchiveProperties port(final int port) {
        this.port = port;
        return this;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public ArchiveProperties username(final String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public ArchiveProperties password(final String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("path", path)
                                        .append("port", port)
                                        .append("username", username)
                                        .append("password", getPasswordStared(password))
                                        .toString();
    }
}