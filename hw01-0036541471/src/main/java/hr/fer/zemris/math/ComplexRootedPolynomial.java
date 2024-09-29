package hr.fer.zemris.math;

public class ComplexRootedPolynomial {

    private final Complex constant;
    private final Complex[] roots;

    public ComplexRootedPolynomial(Complex constant, Complex ...roots) {
        this.constant = constant;
        this.roots = roots;
    }
    
    public Complex apply(Complex z) {
        
        Complex outputNumber = null, delta;
        
        for (int i = 0; i < roots.length; i++) {
            delta = z.sub(this.roots[i]);
            
            if (i == 0) {
                delta = constant.multiply(delta);
            }
            
            if (outputNumber == null) {
                outputNumber = delta;
            } else {
                outputNumber = outputNumber.multiply(delta);
            }
        }
        
        return outputNumber;
    }

    public ComplexPolynomial toComplexPolynomial() {
        ComplexPolynomial result = new ComplexPolynomial(constant);

        for (Complex root : roots) {
            result = result.multiply(new ComplexPolynomial(
                    root.negate(),
                    new Complex(1, 0)
            ));
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(constant).append(")");
        for (Complex root : roots) {
            sb.append("*(z-(").append(root).append("))");
        }

        return sb.toString();
    }

    // finds index of the closest root for given complex number z that is within
    // threshold; if there is no such root, returns -1
    // first root has index 0, second index 1, etc
    public int indexOfClosestRootFor(Complex z, double threshold) {
        double distance;


        for (int i = 0; i < roots.length; i++) {
            if (roots[i].sub(z).module() <= threshold) {
                return i;
            }
        }

        return -1;
    }
}
