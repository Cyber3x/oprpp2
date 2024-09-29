package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;

public class SumWorker implements IWebWorker {
    private RequestContext context;
    @Override
    public void processRequest(RequestContext context) throws IOException {
        this.context = context;

        int a = parseIntOrDefault("a", 1);
        int b = parseIntOrDefault("b", 2);

        String sum = String.valueOf(a + b);
        context.setTemporaryParameter("zbroj", sum);
        context.setTemporaryParameter("varA", String.valueOf(a));
        context.setTemporaryParameter("varB", String.valueOf(b));
        context.setTemporaryParameter("isNumEven", String.valueOf((a + b) % 2 == 0));
        context.dispatch("/private/pages/calc.smscr");
    }

    private int parseIntOrDefault(String paramName, int defaultValue) {
        try {
            return Integer.parseInt(context.getParameter(paramName));
        } catch (NumberFormatException ignorable) {
            return defaultValue;
        }
    }
}
