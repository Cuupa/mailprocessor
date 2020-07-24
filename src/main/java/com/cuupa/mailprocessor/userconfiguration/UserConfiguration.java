package com.cuupa.mailprocessor.userconfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class UserConfiguration {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("locale")
    @Expose
    private Locale locale;

    @SerializedName("locale")
    @Expose
    private boolean reload;

    @SerializedName("emailproperties")
    @Expose
    private EmailProperties emailProperties;

    @SerializedName("scanproperties")
    @Expose
    private ScanProperties scanProperties;

    @SerializedName("archiveproperties")
    @Expose
    private ArchiveProperties archiveProperties;

    @SerializedName("reminderproperties")
    @Expose
    private ReminderProperties reminderProperties;

    public String getUsername() {
        return username;
    }

    @NotNull
    public void setUsername(final String username) {
        this.username = username;
    }

    @NotNull
    public UserConfiguration username(final String username) {
        this.username = username;
        return this;
    }

    public Locale getLocale() {
        return locale;
    }

    @NotNull
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    @NotNull
    public UserConfiguration locale(final Locale locale) {
        this.locale = locale;
        return this;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(final boolean reload) {
        this.reload = reload;
    }

    @NotNull
    public UserConfiguration reload(final boolean reload) {
        this.reload = reload;
        return this;
    }

    public EmailProperties getEmailProperties() {
        return emailProperties != null ? emailProperties : new EmailProperties();
    }

    @NotNull
    public void setEmailProperties(final EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @NotNull
    public UserConfiguration emailProperties(final EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
        return this;
    }

    public ScanProperties getScanProperties() {
        return scanProperties != null ? scanProperties : new ScanProperties();
    }

    @NotNull
    public void setScanProperties(final ScanProperties scanProperties) {
        this.scanProperties = scanProperties;
    }

    @NotNull
    public UserConfiguration scanProperties(final ScanProperties scanProperties) {
        this.scanProperties = scanProperties;
        return this;
    }

    public ArchiveProperties getArchiveProperties() {
        return archiveProperties != null ? archiveProperties : new ArchiveProperties();
    }

    @NotNull
    public void setArchiveProperties(final ArchiveProperties archiveProperties) {
        this.archiveProperties = archiveProperties;
    }

    @NotNull
    public UserConfiguration archiveProperties(final ArchiveProperties archiveProperties) {
        this.archiveProperties = archiveProperties;
        return this;
    }

    public ReminderProperties getReminderProperties() {
        return reminderProperties != null ? reminderProperties : new ReminderProperties();
    }

    @NotNull
    public void setReminderProperties(final ReminderProperties reminderProperties) {
        this.reminderProperties = reminderProperties;
    }

    @NotNull
    public UserConfiguration reminderProperties(final ReminderProperties reminderProperties) {
        this.reminderProperties = reminderProperties;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("username", username)
                                        .append("locale", locale)
                                        .append("reload", reload)
                                        .append("emailproperties", emailProperties)
                                        .append("scanproperties", scanProperties)
                                        .append("archiveproperties", archiveProperties)
                                        .append("reminderproperties", reminderProperties)
                                        .toString();
    }
}
