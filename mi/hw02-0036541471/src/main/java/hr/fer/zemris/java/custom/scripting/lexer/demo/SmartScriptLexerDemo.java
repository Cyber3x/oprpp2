package hr.fer.zemris.java.custom.scripting.lexer.demo;

import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SmartScriptLexerDemo {
    public static void main(String[] args) throws IOException {
        SmartScriptLexer lexer = new SmartScriptLexer(
                Files.readString(Path.of("scripts/osnovni.smscr"))
        );

        Token currentToken;
        do {
            currentToken = lexer.nextToken();
            System.out.println(currentToken);
        }
        while (currentToken.getType() != TokenType.EOF);
    }
}
