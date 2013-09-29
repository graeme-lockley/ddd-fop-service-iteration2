package com.no9.app.ports;

import com.no9.app.services.TemplateID;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FOPUtils {
    private static FopFactory fopFactory = FopFactory.newInstance();
    private Map<TemplateID, Templates> xslTemplatesCache = Collections.synchronizedMap(new HashMap<TemplateID, Templates>());

    public FOPUtils() {
    }

    public void xmlToPDF(StreamSource xmlSource, Templates template, OutputStream outputStream) throws FOPException, IOException, TransformerException {
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, outputStream);
        Result res = new SAXResult(fop.getDefaultHandler());
        template.newTransformer().transform(xmlSource, res);
    }

    public Templates getXSLTemplate(TemplateID xsltTemplateID) throws TransformerException {
        Templates templates = xslTemplatesCache.get(xsltTemplateID);
        if (templates == null) {
            templates = loadTemplate(xsltTemplateID);
            xslTemplatesCache.put(xsltTemplateID, templates);
        }
        return templates;
    }

    private Templates loadTemplate(TemplateID xsltTemplateID) throws TransformerException {
        URIResolver uriResolver = getResolver();
        Source xsltSource = uriResolver.resolve(xsltTemplateID.getTemplateURI(), null);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(uriResolver);
        return transformerFactory.newTemplates(xsltSource);
    }

    private ResourceResolver getResolver() {
        return new ResourceResolver();
    }
}