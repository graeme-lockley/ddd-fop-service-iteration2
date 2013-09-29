package com.no9.app.services;

import com.no9.app.adaptors.FOPRenderService;

public class ServiceRegistry {
    private static final RenderService renderService = new FOPRenderService();

    public static RenderService renderService() {
        return renderService;
    }
}
