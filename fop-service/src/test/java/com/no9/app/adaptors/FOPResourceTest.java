package com.no9.app.adaptors;

import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FOPResourceTest {
    private static final String BLANK_XSL_TEMPLATE = "  ";
    private static final String INVALID_XSL_TEMPLATE = "as";
    private static final String VALID_XSL_TEMPLATE = "resource://HelloWorld.xsl";
    private static final String BLANK_XML_TEMPLATE = "  ";
    private static final String INVALID_XML_CONTENT = "<root></roots>";
    private static final String VALID_XML_CONTENT = "<root></root>";

    private FOPResource fopResource = new FOPResource();

    @Test
    public void should_throw_exception_if_no_template_is_passed() throws Exception {
        try {
            fopResource.process(BLANK_XSL_TEMPLATE, BLANK_XML_TEMPLATE);
            fail();
        } catch (WebApplicationException ex) {
            assertResponseEquals(400, "Parameter template has not been set.", ex);
        }
    }

    @Test
    public void should_throw_exception_if_no_xml_content_is_passed() throws Exception {
        try {
            fopResource.process(INVALID_XSL_TEMPLATE, BLANK_XML_TEMPLATE);
            fail();
        } catch (WebApplicationException ex) {
            assertResponseEquals(400, "Parameter xmlContent has not been set.", ex);
        }
    }

    @Test
    public void should_throw_exception_if_an_invalid_template_is_passed() throws Exception {
        try {
            StreamingOutput streamingOutput = fopResource.process(INVALID_XSL_TEMPLATE, VALID_XML_CONTENT);
            processOutput(streamingOutput);
            fail();
        } catch (WebApplicationException ex) {
            assertResponseEquals(400, "javax.xml.transform.TransformerException: Unable to resolve resource where the href does not start with resource://", ex);
        }
    }

    @Test
    public void should_throw_exception_if_an_ill_formed_xml_is_passed() throws Exception {
        try {
            StreamingOutput streamingOutput = fopResource.process(VALID_XSL_TEMPLATE, INVALID_XML_CONTENT);
            processOutput(streamingOutput);
            fail();
        } catch (WebApplicationException ex) {
            assertResponseEquals(400, "javax.xml.transform.TransformerException: The end-tag for element type \"root\" must end with a '>' delimiter.", ex);
        }
    }

    @Test
    public void should_all_work_if_arguments_are_valid() throws Exception {
        StreamingOutput streamingOutput = fopResource.process(VALID_XSL_TEMPLATE, VALID_XML_CONTENT);
        String result = processOutput(streamingOutput);

        assertTrue(result.startsWith("%PDF-1.4"));
    }

    private void assertResponseEquals(int statusCode, String message, WebApplicationException ex) {
        assertEquals(statusCode, ex.getResponse().getStatus());
        assertEquals(message, ex.getResponse().getEntity().toString());
    }

    private String processOutput(StreamingOutput streamingOutput) throws IOException {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            streamingOutput.write(outputStream);
            return outputStream.toString();
        }
    }
}
