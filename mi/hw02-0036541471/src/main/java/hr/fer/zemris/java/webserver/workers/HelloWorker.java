package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        context.setMimeType("text/html");

        String name = context.getParameter("name");
        context.write("<html><body>");
        context.write("<h0>Hello!!!</h1>");
        context.write("<p>Now is: " + simpleDateFormat.format(now) + "</p>");
        if (name == null || name.trim().isEmpty()) {
            context.write("<p>You did not send me your name!</p>");
        } else {
            context.write("<p>Your name has " + name.trim().length()
                    + " letters.</p>");
        }
        context.write("</body></html>");
    }
}
