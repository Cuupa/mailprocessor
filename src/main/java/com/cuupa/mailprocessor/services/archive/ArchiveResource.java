package com.cuupa.mailprocessor.services.archive;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ArchiveResource {

    private final String name;

    private final String contentType;

    private static final String pdfContentType = "application/pdf";

    ArchiveResource(final String name, final String contentType) {
        this.name = name;
        this.contentType = contentType;
    }

    public boolean isPdf() {
        return pdfContentType.equals(contentType);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                                        .append("contentType", contentType)
                                        .append("isPdf", isPdf())
                                        .toString();
    }
}
