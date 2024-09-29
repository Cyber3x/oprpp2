package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Complex {

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double module() {
        return Math.sqrt(Math.pow(this.re, 2) + Math.pow(this.im, 2));
    }

    public Complex multiply(Complex other) {
        return new Complex(
                this.re * other.re - this.im * other.im,
                this.re * other.im + this.im * other.re
        );
    }

    public Complex divide(Complex other) {
        double divisor = Math.pow(other.re, 2) + Math.pow(other.im, 2);

        double newRe = (this.re * other.re + this.im * other.im) / divisor;
        double newIm = (this.im * other.re - this.re * other.im) / divisor;

        return new Complex(newRe, newIm);
    }

    public Complex add(Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex sub(Complex other) {
        return new Complex(this.re - other.re, this.im - other.im);
    }

    public Complex negate() {
        return new Complex(-this.re, -this.im);
    }

    public Complex power(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be not negative");

        double angle = Math.atan2(this.im, this.re);
        double module = this.module();

        double newModule = Math.pow(module, n);
        double newAngle = angle * n;

        double newRe = newModule * Math.cos(newAngle);
        double newIm = newModule * Math.sin(newAngle);

        return new Complex(newRe, newIm);
    }

    public List<Complex> root(int n) {
        if (n < 1) throw new IllegalArgumentException("n must be positive");

        double angle = Math.atan2(this.im, this.re);
        double module = this.module();

        double newModule = Math.pow(module, 1. / n);
        double newAngle = 0;

        ArrayList<Complex> roots = new ArrayList<Complex>(n);
        for (int k = 0; k < n; k++) {
            newAngle = (angle * 2 * Math.PI * k) / n;

            roots.add(new Complex(
                    newModule * Math.cos(newAngle),
                    newModule * Math.sin(newAngle))
            );
        }

        return roots;
    }


    @Override
    public String toString() {
//        if (this.im == 0) {
//            return String.valueOf(this.re);
//        }
        String prefix = this.im >= 0 ? "+" : "-";
        return this.re + prefix + "i" + Math.abs(this.im);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.re, re) == 0 && Double.compare(complex.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
}
