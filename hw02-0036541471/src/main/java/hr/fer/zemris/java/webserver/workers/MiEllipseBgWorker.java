package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MiEllipseBgWorker implements IWebWorker {
    private static final int BORDER_WIDTH = 10;
    @Override
    public void processRequest(RequestContext context) throws IOException {
        context.setMimeType("image/png");
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 100);
        g.setColor(MIUtils.generateRandomColor());
        g.fillOval(0, 0, 100, 100);
        g.setColor(MIUtils.generateRandomColor());
        g.fillOval(BORDER_WIDTH, BORDER_WIDTH, 100 - 2 * BORDER_WIDTH, 100 - 2 * BORDER_WIDTH);
        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bos);
        context.write(bos.toByteArray());
    }
}
