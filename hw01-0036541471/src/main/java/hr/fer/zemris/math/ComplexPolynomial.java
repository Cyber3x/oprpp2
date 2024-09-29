package hr.fer.zemris.math;

import java.util.Arrays;

public class ComplexPolynomial {

    private final Complex[] factors;

    // z0, z1, z2, ...
    public ComplexPolynomial(Complex ...factors) {
        this.factors = factors;
    }

    public short order() {
        return (short) this.factors.length;
    }

    public ComplexPolynomial multiply(ComplexPolynomial other) {
        short thisOrder = this.order();
        short otherOrder = other.order();
        Complex[] coeffs = new Complex[thisOrder + otherOrder - 1];

        for (int i = 0; i < thisOrder; i++) {
            for (int j = 0; j < otherOrder; j++) {
                Complex result = this.factors[i].multiply(other.factors[j]);

                if (coeffs[i + j] == null) {
                    coeffs[i + j] = result;
                } else {
                    coeffs[i + j] = coeffs[i + j].add(result);
                }
            }
        }

        return new ComplexPolynomial(coeffs);
    }

    public ComplexPolynomial derive() {
        short order = this.order();
        Complex[] newFactors = new Complex[order - 1];

        for (int i = order - 1; i > 0; i--) {
            newFactors[i - 1] = factors[i].multiply(new Complex(i, 0));
        }

        return new ComplexPolynomial(newFactors);
    }

    public Complex apply(Complex z) {
        Complex zPower, multiplication;
        Complex result = Complex.ZERO;
        for (int i = 0; i < this.factors.length; i++) {
            zPower = z.power(i);

            multiplication = zPower.multiply(factors[i]);
            result = result.add(multiplication);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.factors.length - 1; i >= 0; i--) {
            sb.append("(").append(this.factors[i]).append(")");
            if (i > 0) {
                sb.append("*z^").append(i).append("+");
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexPolynomial that = (ComplexPolynomial) o;
        return Arrays.equals(factors, that.factors);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(factors);
    }
}
