package hr.fer.zemris.java.custom.scripting.lexer;

public enum TokenType {
    TEXT,           // value string
    TAG_START,      // value null
    TAG_END,        // value null
    TAG_NAME,       // value string, = or valid variable name
    VARIABLE_NAME,  // value string, start by letter and follow by zero or more letters didgits or underscores
    FUNCTION_NAME,  // value string, start by @ after follows a letter and then zero or more letters didgits or underscores
    OPERATOR,       // value string, + - * / ^
    STRING,         // value string
    NUMBER,         // value string (not parsed), can be negative
    EOF
}
