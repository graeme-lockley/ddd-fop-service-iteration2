package com.no9.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = getServer();

        server.start();
        server.join();
    }

    protected static Server getServer() {
        Server server = new Server(8081);

        WebAppContext context = newWebContext();

        server.setHandler(context);

        return server;
    }

    private static WebAppContext newWebContext() {
        WebAppContext context = new WebAppContext();

        context.setDescriptor(getContextPath("src/main/webapp/WEB-INF/web.xml"));
        context.setResourceBase(getContextPath("src/main/webapp"));
        context.setContextPath("/");
        context.setParentLoaderPriority(true);

        return context;
    }

    private static String getContextPath(String suffix) {
        return new File(getProjectBase(), suffix).getPath();
    }

    private static File getProjectBase() {
        ProtectionDomain protectionDomain = Main.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        return new File(location.toExternalForm(), "../..");
    }
}