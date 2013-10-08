package com.no9.app.services;

import com.no9.app.adaptors.RemoteRestRenderService;

public class ServiceRegistry {
    private static RenderService renderService = new RemoteRestRenderService("http://localhost:8081");

    protected void setRenderService(RenderService renderService) {
        this.renderService = renderService;
    }

    public static RenderService renderService() {
        return renderService;
    }
}
