package hr.fer.zemris.java.webserver;

import java.io.IOException;

public interface IWebWorker {
    void processRequest(RequestContext context) throws IOException;
}
