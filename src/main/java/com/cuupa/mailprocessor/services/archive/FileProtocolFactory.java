package com.cuupa.mailprocessor.services.archive;

import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * Determines which FileProtocol to use. Currently supports WebDAV and local storage for windows and UNIX
 */
public class FileProtocolFactory {

    private static final Pattern patternWindows = Pattern.compile("([A-Z]):\\\\[a-zA-Z0-9\\.\\\\]*");

    private static final Pattern patternUnix = Pattern.compile("/[a-zA-Z0-9/]*");

    public static FileProtocol getForPath(final String path) {
        Assert.hasText(path, "no path provided");

        if (isWebDav(path)) {
            return new WebDavArchiver();
        } else if (isLocal(path)) {
            return new LocalArchiver();
        }
        return null;
    }

    private static boolean isLocal(String path) {
        return patternWindows.matcher(path.replace("/", "\\")).matches() || patternUnix.matcher(path).matches();
    }

    private static boolean isWebDav(String path) {
        return path.startsWith("http://") || path.startsWith("https://");
    }

}
