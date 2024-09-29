package hr.fer.zemris.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonP1 {
    private static final HashMap<String, Integer> newtonArgs = new HashMap<>(2);
    private static final ArrayList<Complex> roots = new ArrayList<>();

    public static void main(String[] args) {
        parseArgs(args);

        newtonArgs.putIfAbsent("workers", Runtime.getRuntime().availableProcessors());
        newtonArgs.putIfAbsent("tracks", Runtime.getRuntime().availableProcessors() * 4);

        System.out.printf("Using %d thread and %d jobs%n", newtonArgs.get("workers"), newtonArgs.get("tracks"));

        System.out.println(
                "Welcome to Newton-Raphson iteration-based fractal viewer.\nPlease enter at least two roots, one root per line. Enter 'done' when done.");
        Scanner sc = new Scanner(System.in);

        int rootNumber = 1;
        while (true) {
            System.out.print("Root " + rootNumber + "> ");
            String currentLine = sc.nextLine();
            if (currentLine.equals("done")) {
                if (roots.size() >= 2) {
                    System.out.println(
                            "Image of fractal will appear shortly. Thank you."
                    );
                    break;
                } else {
                    System.out.println("You need to enter at least two roots!");
                    System.exit(1);
                }
            }
            String[] lineParts = currentLine.split(" ");
            if (lineParts.length != 1 && lineParts.length != 3) {
                System.out.println("Wrong input format");
                System.exit(1);
            }

            if (lineParts.length == 1) {
                String number = lineParts[0];
                if (number.contains("i")) {
                    if (number.length() == 1 || (number.length() == 2 && number.charAt(0) == '-')) {
                        number = number.replace("i", "1");
                    } else {
                        number = number.replace("i", "");
                    }
                    try {
                        int im = Integer.parseInt(number);
                        roots.add(new Complex(0, im));
                    } catch (NumberFormatException ignored) {
                        System.out.println("Wrong input");
                        System.exit(1);
                    }
                } else {
                    try {
                        int re = Integer.parseInt(number);
                        roots.add(new Complex(re, 0));
                    } catch (NumberFormatException ignored) {
                        System.out.println("Wrong input");
                        System.exit(1);
                    }
                }
            } else {
                String reStr = lineParts[0];
                String op = lineParts[1];
                String imStr = lineParts[2];

                if (!imStr.contains("i") || (!op.equals("-") && !op.equals("+"))) {
                    System.out.println("Wrong input");
                    System.exit(1);
                }

                if (imStr.length() == 1) {
                    imStr = imStr.replace("i", "1");
                } else {
                    imStr = imStr.replace("i", "");
                }

                try {
                    int re = Integer.parseInt(reStr);
                    int im = Integer.parseInt(imStr);

                    if (op.equals("+")) {
                        roots.add(new Complex(re, im));
                    } else {
                        roots.add(new Complex(re, -im));
                    }
                } catch (NumberFormatException ignored) {
                    System.out.println("Wrong input");
                    System.exit(1);
                }
            }
            rootNumber += 1;
        }

        Complex[] a = new Complex[roots.size()];
        a = roots.toArray(a);

        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(1, 0), a);

        FractalViewer.show(new MojProducer(0.002, 16 * 16, 0.002, crp));
    }

    private static void putIfNotSet(String key, int value) {
        if (newtonArgs.get(key) != null) {
            System.out.printf("%s already defined", key);
            System.exit(1);
        }
        newtonArgs.put(key, value);
    }

    private static void parseArgumentPair(String name, String value) {
        int valueInt;

        try {
            valueInt = Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            System.out.println("param must be a number");
            System.exit(1);
            return;
        }

        if (valueInt < 1) {
            System.out.println("min value is 1");
            System.exit(1);
        }

        putIfNotSet(name, valueInt);
    }

    public static void parseArgs(String[] args) {
        boolean skip = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (skip) {
                skip = false;
                continue;
            }

            if (arg.contains("=")) {
                String[] parts = arg.split("=");

                String name = parts[0];
                String value = parts[1];

                switch (name) {
                    case "--workers" -> {
                        parseArgumentPair("workers", value);
                    }
                    case "--tracks" -> {
                        parseArgumentPair("tracks", value);
                    }
                    case "--mintracks" -> {
                        parseArgumentPair("mintracks", value);
                    }
                    default -> {
                        System.out.println("param not suppoerted");
                        System.exit(1);
                    }
                }
            } else {
                if (i + 1 == args.length) {
                    System.out.printf("missing %s value", arg);
                    System.exit(1);
                }

                String value = args[i + 1];


                switch (arg) {

                    case "-w" -> {
                        System.out.println("hi");
                        parseArgumentPair("workers", value);
                    }
                    case "-t" -> {
                        parseArgumentPair("tracks", value);
                    }
                    case "-m" -> {
                        parseArgumentPair("mintracks", value);
                    }
                    default -> {
                        System.out.println("param not supported");
                        System.exit(1);
                    }
                }
                skip = true;
            }
        }
    }

    public static class MojProducer implements IFractalProducer {
        private final double convergenceThreshold;
        private final int maxIterCount;
        private final double rootThreshold;
        private final ComplexRootedPolynomial complexRootedPolynomial;
        private final ComplexPolynomial polynomial;
        private ExecutorService pool;

        public MojProducer(double convergenceThreshold, int maxIterCount, double rootThreshold, ComplexRootedPolynomial crp) {
            this.convergenceThreshold = convergenceThreshold;
            this.maxIterCount = maxIterCount;
            this.rootThreshold = rootThreshold;

            this.complexRootedPolynomial = crp;
            this.polynomial = complexRootedPolynomial.toComplexPolynomial();
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo,
                            IFractalResultObserver observer,
                            AtomicBoolean cancel
        ) {
            System.out.println("Započinjem izračune...");

            int m = 16 * 16 * 16;
            short[] data = new short[width * height];

            int numberOfTracks = newtonArgs.get("tracks");
            if (numberOfTracks > height) {
                numberOfTracks = height;
            }
            int numberOfRowsPerTrack = height / numberOfTracks;

            ArrayList<Future<?>> results = new ArrayList<>(numberOfTracks);
            int offset;

            for(int i = 0; i < numberOfTracks; i++) {
                int yMin = i * numberOfRowsPerTrack;
                int yMax = Math.min(height - 1, (i+1) * numberOfRowsPerTrack - 1);

                offset = numberOfRowsPerTrack * width * i;

                PosaoIzracuna posao = new PosaoIzracuna(
                        reMin, reMax, imMin, imMax, width, height,
                        yMin, yMax, m, data, cancel,
                        complexRootedPolynomial, convergenceThreshold, maxIterCount, rootThreshold,
                        offset);

                results.add(pool.submit(posao));
            }

            for (Future<?> result : results) {
                while (true) {
                    try {
                        result.get();
                        break;
                    } catch (InterruptedException ignored) {
                    } catch (ExecutionException e) {
                        break;
                    }
                }
            }

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, polynomial.order(), requestNo);
        }

        @Override
        public void setup() {
            this.pool = Executors.newFixedThreadPool(newtonArgs.get("workers"));
        }

        @Override
        public void close() {
            this.pool.shutdownNow();
        }
    }

}
