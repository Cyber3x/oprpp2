package hr.fer.zemris.java.custom.scripting.lexer;

public class Token {
    private TokenType type;
    private Object value;

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("TOKEN TYPE\t [%1s]: %2s", type, value);
    }
}
