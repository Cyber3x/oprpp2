package hr.fer.oprpp2.servlets.mi;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Ellipses extends HttpServlet {
    private static final int BORDER_WIDTH = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 100);
        g.setColor(Utils.generateRandomColor());
        g.fillOval(0, 0, 100, 100);
        g.setColor(Utils.generateRandomColor());
        g.fillOval(BORDER_WIDTH, BORDER_WIDTH, 100 - 2 * BORDER_WIDTH, 100 - 2 * BORDER_WIDTH);
        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bos);
        resp.setContentLength(bos.toByteArray().length);
        resp.getOutputStream().write(bos.toByteArray());
    }
}
