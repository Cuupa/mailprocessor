package com.cuupa.mailprocessor.userconfiguration;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ConfigurationProperties implements Serializable {

    protected String getPasswordStared(final String password) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return Strings.repeat("*", password.toCharArray().length);
    }
}
