package com.no9.app.adaptors;

import com.no9.app.services.RenderException;
import com.no9.app.services.ServiceRegistry;
import com.no9.app.services.TemplateID;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.filters.StringInputStream;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

@Path("/fop")
public class FOPResource {
    @GET
    public String render() {
        return "FOPResource alive and kicking";
    }

    @POST
    @Consumes("application/xml")
    @Produces("application/pdf")
    public StreamingOutput process(@QueryParam("template") final String template, final String xmlContent) {
        validateArgumentIsNotBlank(template, "template");
        validateArgumentIsNotBlank(xmlContent, "xmlContent");

        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    ServiceRegistry
                            .renderService()
                            .toPDF(new TemplateID(template), new StringInputStream(xmlContent), output);
                } catch (RenderException ex) {
                    throwWebApplicationException(Response.Status.BAD_REQUEST, ex.getMessage());
                }
            }
        };
    }

    private void validateArgumentIsNotBlank(String value, String name) {
        if (StringUtils.isBlank(value)) {
            throwWebApplicationException(Response.Status.BAD_REQUEST, "Parameter " + name + " has not been set.");
        }
    }

    private void throwWebApplicationException(Response.Status status, String errorMessage) {
        throw new WebApplicationException(Response.status(status).entity(errorMessage).build());
    }
}
