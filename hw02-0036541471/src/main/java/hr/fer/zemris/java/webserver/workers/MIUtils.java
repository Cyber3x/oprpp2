package hr.fer.zemris.java.webserver.workers;

import java.awt.*;
import java.util.Random;

public class MIUtils {
    public static Color generateRandomColor() {
        Random r = new Random();

        int red = r.nextInt(256);
        int green = r.nextInt(256);
        int blue = r.nextInt(256);

        return new Color(red, green, blue);
    }
}
