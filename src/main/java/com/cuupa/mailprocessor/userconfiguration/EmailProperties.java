package com.cuupa.mailprocessor.userconfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class EmailProperties extends ConfigurationProperties {

    @SerializedName("servername")
    @Expose
    private String servername;

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("protocol")
    @Expose
    private String protocol;

    @SerializedName("labels")
    @Expose
    private List<String> labels;

    @SerializedName("markasread")
    @Expose
    private boolean markAsRead;

    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    public String getServername() {
        return servername;
    }

    public void setServername(final String servername) {
        this.servername = servername;
    }

    public EmailProperties servername(final String servername) {
        this.servername = servername;
        return this;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public EmailProperties port(final int port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public EmailProperties username(final String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public EmailProperties password(final String password) {
        this.password = password;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    public EmailProperties protocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(final List<String> labels) {
        this.labels = labels;
    }

    public EmailProperties labels(final List<String> labels) {
        this.labels = labels;
        return this;
    }

    public boolean isMarkAsRead() {
        return markAsRead;
    }

    public void setMarkAsRead(final boolean markAsRead) {
        this.markAsRead = markAsRead;
    }

    public EmailProperties markAsRead(final boolean markAsRead) {
        this.markAsRead = markAsRead;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public EmailProperties enabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("servername", servername)
                                        .append("port", port)
                                        .append("username", username)
                                        .append("password", getPasswordStared(password))
                                        .append("protocol", protocol)
                                        .append("labels", labels)
                                        .append("markasread", markAsRead)
                                        .append("enabled", enabled)
                                        .toString();
    }
}
