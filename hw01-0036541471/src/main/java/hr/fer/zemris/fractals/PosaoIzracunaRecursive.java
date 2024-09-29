package hr.fer.zemris.fractals;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

public class PosaoIzracunaRecursive extends RecursiveAction {
    double reMin;
    double reMax;
    double imMin;
    double imMax;
    int width;
    int height;
    int yMin;
    int yMax;
    int mintracks;
    int m;
    int offset;
    short[] data;
    AtomicBoolean cancel;
    public static PosaoIzracunaRecursive NO_JOB = new PosaoIzracunaRecursive();

    private ComplexPolynomial polynomial;
    private ComplexPolynomial polynomialDerived;
    private ComplexRootedPolynomial polynomialRooted;

    private double convergenceThreshold;
    private int maxIterCount;
    private double rootThreshold;

    private PosaoIzracunaRecursive() {
    }

    public PosaoIzracunaRecursive(int mintracks, double reMin, double reMax, double imMin,
                                  double imMax, int width, int height, int yMin, int yMax,
                                  int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial crp,
                                  double convergenceThreshold, int maxIterCount, double rootThreshold, int offset) {
        super();
        this.reMin = reMin;
        this.reMax = reMax;
        this.imMin = imMin;
        this.imMax = imMax;
        this.width = width;
        this.height = height;
        this.yMin = yMin;
        this.yMax = yMax;
        this.m = m;
        this.data = data;
        this.cancel = cancel;
        this.mintracks = mintracks;
        this.offset = offset;

        this.polynomialRooted = crp;
        this.polynomial = this.polynomialRooted.toComplexPolynomial();
        this.polynomialDerived = polynomial.derive();
        this.convergenceThreshold = convergenceThreshold;
        this.maxIterCount = maxIterCount;
        this.rootThreshold = rootThreshold;

    }

    @Override
    public void compute() {
        int totalHeight = yMax - yMin + 1;

        if (totalHeight <= mintracks) {
            directCompute();
            return;
        }

        int upperHeight = totalHeight / 2;

        int upperYMin = yMin;
        int upperYMax = yMin + upperHeight - 1;

        int lowerYMin = yMin + upperHeight;
        int lowerYMax = yMax;

        int upperOffset = offset;
        int lowerOffset = offset + upperHeight * width;

        PosaoIzracunaRecursive p1 = new PosaoIzracunaRecursive(mintracks, reMin, reMax, imMin, imMax, width, height, upperYMin, upperYMax, m, data, cancel, polynomialRooted, convergenceThreshold, maxIterCount, rootThreshold, upperOffset);
        PosaoIzracunaRecursive p2 = new PosaoIzracunaRecursive(mintracks, reMin, reMax, imMin, imMax, width, height, lowerYMin, lowerYMax, m, data, cancel, polynomialRooted, convergenceThreshold, maxIterCount, rootThreshold, lowerOffset);
        invokeAll(p1, p2);
    }

    public void directCompute() {

        Complex zn, numerator, denominator, znold, fraction;
        int index;

        double module, iters, cre, cim;

        for (int y = yMin; y <= yMax; y++) {
            if (cancel.get()) break;
            for (int x = 0; x < width; x++) {
                cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
                zn = new Complex(cre, cim);

                iters = 0;

                do {
                    numerator = polynomial.apply(zn);
                    denominator = polynomialDerived.apply(zn);
                    znold = zn;
                    fraction = numerator.divide(denominator);
                    zn = zn.sub(fraction);
                    module = znold.sub(zn).module();
                    iters++;
                } while (module > this.convergenceThreshold && iters < this.maxIterCount );
                index = this.polynomialRooted.indexOfClosestRootFor(zn, this.rootThreshold);
                data[offset++] = (short) (index + 1);
            }
        }
    }
}