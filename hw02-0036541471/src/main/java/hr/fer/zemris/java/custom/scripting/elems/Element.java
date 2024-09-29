package hr.fer.zemris.java.custom.scripting.elems;

public abstract class Element {
    public String asText() {
        return "";
    }

    @Override
    public String toString() {
        return this.asText();
    }

}
