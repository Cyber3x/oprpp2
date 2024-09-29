package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;

public class Home implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws IOException {
        String bgColor = context.getPersistentParameter("bgcolor");
        if (bgColor != null) context.setTemporaryParameter("background", bgColor);
        context.dispatch("/private/pages/home.smscr");
    }
}
