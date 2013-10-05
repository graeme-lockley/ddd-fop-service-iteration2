package com.no9.app;

import com.no9.app.adaptors.Resource;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {
    Server server;
    private final Resource resource = new Resource("Hello.xml");
    private static final String VALID_URL = "http://localhost:8081/api/fop?template=resource://HelloWorld.xsl";
    private static final String URL_WITH_NO_TEMPLATE = "http://localhost:8081/api/fop";

    @Before
    public void setUp() throws Exception {
        server = Main.getServer();
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void should_ensure_that_the_server_is_configured_to_return_a_valid_PDF() throws Exception {
        Content content = executeRequest(VALID_URL).returnContent();

        assertEquals("application/pdf", content.getType().toString());
        assertTrue(content.asString().startsWith("%PDF-1.4"));
    }

    @Test
    public void should_ensure_that_the_server_returns_an_error_if_no_template_is_set() throws Exception {
        HttpResponse httpResponse = executeRequest(URL_WITH_NO_TEMPLATE).returnResponse();

        assertEquals("Parameter template has not been set.", IOUtils.toString(httpResponse.getEntity().getContent()));
        assertEquals(400, httpResponse.getStatusLine().getStatusCode());
    }

    private Response executeRequest(String url) throws IOException {
        return Request.Post(url)
                .useExpectContinue()
                .version(HttpVersion.HTTP_1_1)
                .bodyString(resource.toString(), ContentType.create("application/xml"))
                .execute();
    }
}
