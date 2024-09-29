package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CircleWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws IOException {
        context.setMimeType("image/png");
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setBackground(Color.yellow);
        g2d.setColor(Color.red);
        g2d.fillOval(0, 0, 200, 200);
        g2d.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bos);
        context.write(bos.toByteArray());
    }
}
