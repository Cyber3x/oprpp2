package hr.fer.zemris.java.custom.scripting.elems;

public class ElementFunction extends Element {
    private final String name;

    public ElementFunction(String name) {
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
    public String toString() {
        return this.asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementFunction that = (ElementFunction) o;
        return name.equals(that.name);
    }
}
