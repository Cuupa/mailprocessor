package com.cuupa.mailprocessor;

import com.cuupa.mailprocessor.userconfiguration.UserConfiguration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MailprocessorConfiguration {

    private static final Log LOG = LogFactory.getLog(MailprocessorConfiguration.class);

    private final List<UserConfiguration> configurations = new ArrayList<>();

    public MailprocessorConfiguration(String configpath) {
        try {
            Type listType = new TypeToken<ArrayList<UserConfiguration>>() {

            }.getType();
            configurations.addAll(new Gson().fromJson(Files.readString(Paths.get(configpath)), listType));
        } catch (IOException ioException) {
            LOG.error("Couldn't read config file", ioException);
        }
    }

    public List<String> getUsers() {
        return configurations.stream().map(UserConfiguration::getUsername).collect(Collectors.toList());
    }

    public List<UserConfiguration> getConfigurations() {
        return List.copyOf(configurations);
    }

    public UserConfiguration getConfigurationForUser(final String user) {
        return configurations.stream()
                             .filter(config -> config.getUsername().equals(user))
                             .findAny()
                             .orElse(new UserConfiguration());
    }
}
