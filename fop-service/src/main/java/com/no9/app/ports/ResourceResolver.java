package com.no9.app.ports;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class ResourceResolver implements URIResolver {
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        if (href.startsWith("resource://")) {
            String resourceName = href.substring(11);
            ClassLoader loader = getClass().getClassLoader();
            InputStream is = loader.getResourceAsStream(resourceName);
            if (is == null) {
                throw new TransformerException("Unable to locate the resource " + href + ".");
            } else {
                return new StreamSource(is, resourceName);
            }
        } else {
            throw new TransformerException("Unable to resolve resource where the href does not start with resource://");
        }
    }
}
