package com.no9.app.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resource {
    private final String resourceName;

    public Resource(String resourceName) {
        this.resourceName = resourceName;
    }

    public File toFile() {
        return new File(getResourceFileName());
    }

    @SuppressWarnings("ConstantConditions")
    private String getResourceFileName() {
        return getClass().getClassLoader().getResource(resourceName).getFile();
    }

    public InputStream toInputStream() throws FileNotFoundException {
        return new FileInputStream(toFile());
    }
}
