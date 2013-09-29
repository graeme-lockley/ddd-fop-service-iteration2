package com.no9.app.services;

public class TemplateID {
    final private String templateURI;

    public TemplateID(String templateURI) {
        this.templateURI = templateURI;
    }

    public String getTemplateURI() {
        return templateURI;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object other) {
        return sameObject(other) || notNullAndSameClass(other) && contentEquals((TemplateID) other);
    }

    private boolean sameObject(Object other) {
        return this == other;
    }

    private boolean notNullAndSameClass(Object other) {
        return other != null && getClass() == other.getClass();
    }

    private boolean contentEquals(TemplateID other) {
        return templateURI.equals(other.templateURI);
    }

    @Override
    public int hashCode() {
        return templateURI.hashCode();
    }
}
