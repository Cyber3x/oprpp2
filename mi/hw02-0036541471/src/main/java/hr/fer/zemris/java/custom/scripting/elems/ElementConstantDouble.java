package hr.fer.zemris.java.custom.scripting.elems;

public class ElementConstantDouble extends Element {
    private double value;


    public ElementConstantDouble(double value) {
        this.value = value;
    }


    public double getValue() {
        return value;
    }

    @Override
    public String asText() {
        return String.valueOf(this.value);
    }

    @Override
    public String toString() {
        return this.asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementConstantDouble that = (ElementConstantDouble) o;
        return Double.compare(that.value, value) == 0;
    }
}
