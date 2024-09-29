package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.Map;

public class EchoWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws IOException {
        context.setMimeType("text/html");

        context.write("<html><body><table><thread><tr><th>Name</th><th>Value</th></th></thead>");
        context.write("<tbody>");
        for (Map.Entry<String, String> entry : context.getParameters().entrySet()) {
            context.write(String.format("<tr><td>%s</td><td>%s</td></tr>", entry.getKey(), entry.getValue()));
        }
        context.write("</tbody></table></body></html>");
    }
}
