package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;

public class MiSetVariant implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws IOException {
        String bgVariant = context.getParameter("bgVariant");
        context.setPersistentParameter("bgVariant", bgVariant);
        context.dispatch("/mi/vrijeme");
    }
}
