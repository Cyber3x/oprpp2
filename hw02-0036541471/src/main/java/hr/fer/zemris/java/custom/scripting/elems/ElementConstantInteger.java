package hr.fer.zemris.java.custom.scripting.elems;

public class ElementConstantInteger extends Element {
    private int value;

    public ElementConstantInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String asText() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementConstantInteger that = (ElementConstantInteger) o;
        return value == that.value;
    }

    @Override
    public String toString() {
        return this.asText();
    }
}
