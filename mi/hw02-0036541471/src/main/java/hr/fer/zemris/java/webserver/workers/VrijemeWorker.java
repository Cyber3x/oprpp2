package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class VrijemeWorker implements IWebWorker {
    private static final String BG_SQUARES = "/mi/square";
    private static final String BG_ELLIPSES = "/mi/ellipse";
    @Override
    public void processRequest(RequestContext context) throws IOException {
        String bgVariant = context.getPersistentParameter("bgVariant");

        if (bgVariant == null) {
            bgVariant = "renadom";
        }

        switch (bgVariant) {
            case "squares":
                context.setTemporaryParameter("bgUrl", BG_SQUARES);
                break;
            case "ellipses":
                context.setTemporaryParameter("bgUrl", BG_ELLIPSES);
                break;
            default:
                if (new Random().nextBoolean()) {
                    context.setTemporaryParameter("bgUrl", BG_SQUARES);
                } else {
                    context.setTemporaryParameter("bgUrl", BG_ELLIPSES);
                }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());

        context.setTemporaryParameter("currentTime", currentTime);
        context.dispatch("/private/pages/mi.smscr");
    }
}
