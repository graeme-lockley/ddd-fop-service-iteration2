package com.no9.app.services;

public class RenderException extends Exception {
    public RenderException(Exception exception) {
        super(exception);
    }
}
