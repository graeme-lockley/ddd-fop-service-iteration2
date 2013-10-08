package com.no9.app.adaptors;

import com.no9.app.services.RenderException;
import com.no9.app.services.RenderService;
import com.no9.app.services.TemplateID;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RemoteRestRenderService implements RenderService {
    private final String remoteHostName;

    public RemoteRestRenderService(String remoteHostName) {
        this.remoteHostName = remoteHostName;
    }

    @Override
    public void toPDF(TemplateID templateID, InputStream source, OutputStream output) throws RenderException {
        String url = remoteHostName + "/api/fop?template=" + templateID.getTemplateURI();

        try {
            Response response = Request.Post(url)
                    .useExpectContinue()
                    .version(HttpVersion.HTTP_1_1)
                    .bodyString(IOUtils.toString(source), ContentType.create("application/xml"))
                    .execute();
            IOUtils.copy(response.returnContent().asStream(), output);
        } catch (IOException ex) {
            throw new RenderException(ex);
        }
    }
}
