package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class BgColorWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) {
        String bgcolor = context.getParameter("bgcolor");

        try {
            int color = Integer.parseInt(bgcolor, 16);
            if (color >= 0 && color <= Integer.parseInt("FFFFFF", 16)) {
                context.setTemporaryParameter("mess", "Color is updated");
                context.setPersistentParameter("bgcolor", bgcolor);
                context.setTemporaryParameter("background", bgcolor);
            } else
                throw new NumberFormatException();
        } catch (NumberFormatException ignored) {
            context.setTemporaryParameter("mess", "Color is not updated");
        } finally {
            context.dispatch("/private/pages/redirectHome.smscr");
        }
    }
}
