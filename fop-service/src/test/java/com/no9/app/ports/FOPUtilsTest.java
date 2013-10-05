package com.no9.app.ports;

import com.no9.app.adaptors.Resource;
import com.no9.app.services.TemplateID;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.junit.Assert.*;

public class FOPUtilsTest {
    private FOPUtils fopUtils;
    private static final TemplateID VALID_XSLT_TEMPLATE_ID = new TemplateID("resource://HelloWorld.xsl");
    private static final TemplateID UNKNOWN_XSLT_TEMPLATE_ID = new TemplateID("resource://Bob.xsl");
    private final Resource xmlSourceResource = new Resource("Hello.xml");

    @Before
    public void setUp() throws Exception {
        fopUtils = new FOPUtils();
    }

    @Test
    public void should_get_an_XML_template() throws Exception {
        Templates template = fopUtils.getXSLTemplate(VALID_XSLT_TEMPLATE_ID);

        assertNotNull(template);
    }

    @Test
    public void should_show_that_the_template_is_pulled_out_of_the_cache() throws Exception {
        Templates firstTemplate = fopUtils.getXSLTemplate(VALID_XSLT_TEMPLATE_ID);
        Templates secondTemplate = fopUtils.getXSLTemplate(VALID_XSLT_TEMPLATE_ID);

        assertEquals(firstTemplate, secondTemplate);
    }

    @Test(expected = javax.xml.transform.TransformerException.class)
    public void should_raise_an_exception_for_an_unknown_template() throws Exception {
        fopUtils.getXSLTemplate(UNKNOWN_XSLT_TEMPLATE_ID);
    }

    @Test
    public void should_render_the_XML_content_using_the_XSLT() throws Exception {
        Templates template = fopUtils.getXSLTemplate(VALID_XSLT_TEMPLATE_ID);
        StreamSource xmlSource = xmlAsStreamSource(xmlSourceResource.toFile());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fopUtils.xmlToPDF(xmlSource, template, outputStream);

        String pdfOutput = outputStream.toString();
        assertTrue(pdfOutput.startsWith("%PDF-1.4"));
    }

    private StreamSource xmlAsStreamSource(File xmlFile) {
        return new StreamSource(xmlFile);
    }
}

