package com.no9.app.ui;

import com.no9.app.services.RenderService;
import com.no9.app.services.ServiceRegistry;

class TestServiceRegistry extends ServiceRegistry {
    TestServiceRegistry(RenderService renderService) {
        setRenderService(renderService);
    }
}
