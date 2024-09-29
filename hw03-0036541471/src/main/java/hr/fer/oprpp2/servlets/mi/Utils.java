package hr.fer.oprpp2.servlets.mi;

import java.awt.*;
import java.util.Random;

public class Utils {
    public static Color generateRandomColor() {
        Random r = new Random();

        int red = r.nextInt(256);
        int green = r.nextInt(256);
        int blue = r.nextInt(256);

        return new Color(red, green, blue);
    }
}
