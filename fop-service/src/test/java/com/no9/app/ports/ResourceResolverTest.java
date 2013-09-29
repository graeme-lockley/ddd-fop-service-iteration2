package com.no9.app.ports;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ResourceResolverTest {
    @Test
    public void should_resolve_to_a_valid_resource() throws Exception {
        assertNotNull(new ResourceResolver().resolve("resource://HelloWorld.xsl", null));
    }

    @Test(expected = javax.xml.transform.TransformerException.class)
    public void should_not_resolve_an_invalid_resource() throws Exception {
        assertNotNull(new ResourceResolver().resolve("resource://Bob.xsl", null));
    }

    @Test(expected = javax.xml.transform.TransformerException.class)
    public void should_not_resolve_to_an_invalid_resource_prefix() throws Exception {
        assertNotNull(new ResourceResolver().resolve("http://www.google.com", null));
    }
}
