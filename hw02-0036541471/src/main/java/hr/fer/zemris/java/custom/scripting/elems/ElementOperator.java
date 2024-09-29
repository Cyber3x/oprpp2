package hr.fer.zemris.java.custom.scripting.elems;

public class ElementOperator extends Element {
    private final String symbol;

    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }


    public String getSymbol() {
        return symbol;
    }


    @Override
    public String asText() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return this.asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementOperator that = (ElementOperator) o;
        return symbol.equals(that.symbol);
    }
}
