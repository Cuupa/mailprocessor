package com.cuupa.mailprocessor.userconfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ReminderProperties extends ConfigurationProperties {

    @SerializedName("botname")
    @Expose
    private String botname;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("chatId")
    @Expose
    private String chatId;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    public String getBotname() {
        return botname;
    }

    public void setBotname(final String botname) {
        this.botname = botname;
    }

    public ReminderProperties botname(final String botname) {
        this.botname = botname;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public ReminderProperties token(String token) {
        this.token = token;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(final String chatId) {
        this.chatId = chatId;
    }

    public ReminderProperties chatId(final String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public ReminderProperties url(final String url) {
        this.url = url;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public ReminderProperties enabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("botname", botname)
                                        .append("token", getPasswordStared(token))
                                        .append("chatId", chatId)
                                        .append("url", url)
                                        .append("enabled", enabled)
                                        .toString();
    }
}
