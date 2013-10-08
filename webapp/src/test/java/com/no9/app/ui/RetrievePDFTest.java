package com.no9.app.ui;

import com.no9.app.services.RenderException;
import com.no9.app.services.RenderService;
import com.no9.app.services.TemplateID;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RetrievePDFTest {
    private ServletTester servletTester;
    private HttpTester request;

    @Before
    public void setUp() throws Exception {
        servletTester = new ServletTester();
        servletTester.setContextPath("/");
        servletTester.setResourceBase("src/main/webapp");
        servletTester.addServlet(RetrievePDF.class, "/RetrievePDF");
        servletTester.addServlet(org.mortbay.jetty.servlet.DefaultServlet.class, "/*");
        servletTester.start();

        request = new HttpTester();
        request.setMethod("GET");
        request.setURI("/RetrievePDF");
        request.setVersion("HTTP/1.0");
    }

    @Test
    public void should_get_same_PDF_result_over_multiple_calls() throws Exception {
        new TestServiceRegistry(new RenderService() {
            @Override
            public void toPDF(TemplateID templateID, InputStream source, OutputStream output) throws RenderException {
                try {
                    output.write("%PDF-1.4 - and some other text...".getBytes());
                } catch (IOException ex) {
                    throw new RenderException(ex);
                }
            }
        });

        HttpTester firstResponse = getPDFResponse();
        HttpTester secondResponse = getPDFResponse();

        assertEquals(firstResponse.getContent().length(), secondResponse.getContent().length());
    }

    @Test
    public void should_return_an_error_page() throws Exception {
        new TestServiceRegistry(new RenderService() {
            @Override
            public void toPDF(TemplateID templateID, InputStream source, OutputStream output) throws RenderException {
                throw new RenderException(new Exception("My Exception"));
            }
        });

        HttpTester response = getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("text/html", response.getContentType());
        assertTrue(response.getContent().contains("My Exception"));
    }

    private HttpTester getPDFResponse() throws Exception {
        HttpTester response = getResponse();

        assertEquals(200, response.getStatus());
        assertEquals("application/pdf", response.getContentType());
        assertTrue(response.getContent().startsWith("%PDF-1.4"));

        return response;
    }

    private HttpTester getResponse() throws Exception {
        HttpTester response = new HttpTester();
        response.parse(servletTester.getResponses(request.generate()));

        return response;
    }
}

