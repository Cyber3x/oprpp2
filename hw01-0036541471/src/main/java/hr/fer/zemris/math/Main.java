package hr.fer.zemris.math;

public class Main {
    public static void main(String[] args) {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0),

                // roots
                new Complex(-3, 6),
                new Complex(1, 1)
//              new Complex(2, -3)
        );

//        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
//                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
//        );

        ComplexPolynomial cp = crp.toComplexPolynomial();
//        ComplexPolynomial cpd = cp.derive();

        System.out.println(crp);
        System.out.println(cp);
//        System.out.println(cpd);
//        System.out.println(cpd.apply(new Complex(2, 3)));
    }
}
