package hr.fer.zemris.java.custom.scripting.elems;

public class ElementString extends Element {
    private String value;

    public ElementString(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String asText() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementString that = (ElementString) o;
        return value.equals(that.value);
    }

    @Override
    public String toString() {
        return this.asText();
    }
}
