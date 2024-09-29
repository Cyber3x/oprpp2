package hr.fer.zemris.java.custom.scripting.lexer;

public enum LexerState {
    NORMAL,
    IN_TAG_DEFINITION_WITH_NAME,
    IN_TAG_DEFINITION_LOOKING_FOR_NAME,
}
