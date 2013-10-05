package com.no9.app.adaptors;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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

    @Override
    public String toString() {
        try {
            return FileUtils.readFileToString(toFile());
        } catch (IOException ex) {
            return ex.toString();
        }
    }
}
