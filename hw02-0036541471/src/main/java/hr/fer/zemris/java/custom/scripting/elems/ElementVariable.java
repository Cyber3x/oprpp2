package hr.fer.zemris.java.custom.scripting.elems;

public class ElementVariable extends Element {
    private String name;

    public ElementVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String asText() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementVariable that = (ElementVariable) o;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return this.asText();
    }
}
