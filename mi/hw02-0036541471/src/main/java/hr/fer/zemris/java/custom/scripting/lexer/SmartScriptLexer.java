package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.ArrayList;

public class SmartScriptLexer {
    private final char[] data;
    private Token currentToken;
    private int currentIndex;
    private LexerState currentLexerState;

    /**
     * Creates a new lexer that accepts the source code as a string, methods <code>nextToken</code> can be called to get the next token.
     * @param inputText source code that you want tokenized
     */
    public SmartScriptLexer(String inputText) {
        this.data = inputText.toCharArray();
        this.currentIndex = 0;
        this.currentLexerState = LexerState.NORMAL;
    }

    /**
     * returns the next token
     * @throws SmartScriptLexerException if the next token can't be parsed
     * @return next token
     */
    public Token nextToken() {
        this.extractNextToken();
        return this.getToken();
    }

    /**
     * get the current lexer token without generating a new one
     * @return the current lexer token
     */
    public Token getToken() {
        return this.currentToken;
    }

    public void setLexerState(LexerState newLexerState) {
        if (newLexerState == null) throw new NullPointerException();
        this.currentLexerState = newLexerState;
    }

    private void extractNextToken() {
        if (currentToken != null && currentToken.getType() == TokenType.EOF) throw new SmartScriptLexerException("tokenized the whole file");
        if (data == null) throw new NullPointerException("no data was passed to the lexer");

        //skipBlanks();

        if (currentIndex >= data.length) {
            currentToken = new Token(TokenType.EOF, null);
            return;
        }

        switch (currentLexerState) {
            case NORMAL -> {
                // try to extract tag
                if (data[currentIndex] == '{') {
                    extractTagStart();
                    setLexerState(LexerState.IN_TAG_DEFINITION_LOOKING_FOR_NAME);
                } else {
                    // if not tag then it's text
                    extractText();
                }
            }
            case IN_TAG_DEFINITION_WITH_NAME -> {
                skipBlanks();
                if (data[currentIndex] == '$') {
                    extractTagEnd();
                    setLexerState(LexerState.NORMAL);
                } else if (data[currentIndex] == '@') {
                    extractFunction();
                } else if (data[currentIndex] == '"') {
                    currentIndex++; // we can skip the first "
                    extractString();
                } else if (data[currentIndex] == '-') {
                    extractNumber(true);
                } else if (Character.isDigit(data[currentIndex])) {
                    extractNumber(false);
                } else if (data[currentIndex] == '+' || data[currentIndex] == '*' || data[currentIndex] == '/' || data[currentIndex] == '^') {
                    extractOperator();
                } else if (Character.isLetter(data[currentIndex])) {
                    extractVariable();
                } else {
                    throw new SmartScriptLexerException("Invalid data type in tag definition");
                }
            }
            case IN_TAG_DEFINITION_LOOKING_FOR_NAME -> {
                skipBlanks();
                if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '=') {
                    extractTagName();
                    setLexerState(LexerState.IN_TAG_DEFINITION_WITH_NAME);
                    return;
                }
                throw new SmartScriptLexerException("expecting tag name, found: " + data[currentIndex]);
            }
            default ->
                    throw new SmartScriptLexerException("some lexer state is not covered");
        }
        //throw new SmartScriptLexerException("invalid data order");
    }

    private void extractVariable() {
        int startIndex = currentIndex;
        currentIndex++;

        while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
            currentIndex++;
        }

        String varName = new String(data, startIndex, currentIndex - startIndex);
        currentToken = new Token(TokenType.VARIABLE_NAME, varName);
    }

    private void extractOperator() {
        currentToken = new Token(TokenType.OPERATOR, String.valueOf(data[currentIndex]));
        currentIndex++;
    }

    private void extractNumber(boolean isNegative) {
        int startIndex = currentIndex;
        if (isNegative) {
            currentIndex++;
            if (currentIndex == data.length) throw new SmartScriptLexerException("unable to tokenize negative number, end of data reached");
            if (!Character.isDigit(data[currentIndex])) {
                currentIndex--;
                extractOperator();
                return;
            }
        }

        boolean hasOneDot = false;

        while ((currentIndex < data.length) && ((Character.isDigit(data[currentIndex]) || data[currentIndex] == '.'))) {
            if (data[currentIndex] == '.') {
                if (hasOneDot) {
                    throw new SmartScriptLexerException("unable to tokenize number, already has one dot");
                } else {
                    hasOneDot = true;
                }
            }
            currentIndex++;
        }

        String number = new String(data, startIndex, currentIndex - startIndex);
        currentToken = new Token(TokenType.NUMBER, number);
    }

    private void extractString() {
        boolean escaping = false;

        ArrayList<Character> currentStringChars = new ArrayList<>();

        while (currentIndex < data.length) {
           if (escaping) {
              if (data[currentIndex] == '\\' || data[currentIndex] == '"') {
                  // current chars can go after \
                  currentStringChars.add(data[currentIndex]);
              } else if (data[currentIndex] == 'n') {
                  currentStringChars.add('\n');
              } else if (data[currentIndex] == 't') {
                  currentStringChars.add('\t');
              } else if (data[currentIndex] == 'r') {
                  currentStringChars.add('\r');
              } else {
                  throw new SmartScriptLexerException("invalid character after escape char \\ in string definition");
              }
              escaping = false;
           } else {
               if (data[currentIndex] == '\\') {
                   escaping = true;
               } else if (data[currentIndex] == '"') {
                   currentIndex++;
                   break;
               } else {
                   currentStringChars.add(data[currentIndex]);
               }
           }
            currentIndex++;
        }

        if (escaping) throw new SmartScriptLexerException("unmatched escaping symbol \\ in string definition");

        StringBuilder sb = new StringBuilder();
        for (Character currentStringChar : currentStringChars) {
            sb.append(currentStringChar);
        }
        String string = sb.toString();
        currentToken = new Token(TokenType.STRING, string);
    }


    private void extractFunction() {
        currentIndex++;
        if (currentIndex == data.length) throw new SmartScriptLexerException("unable to tokenize function, end of data reached");
        int startIndex = currentIndex;
        if (!Character.isLetter(data[currentIndex++])) throw new SmartScriptLexerException("invalid function name, function must start with a letter");

        while ((currentIndex < data.length) && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
            currentIndex++;
        }

        String functionName = new String(data, startIndex, currentIndex - startIndex);
        currentToken = new Token(TokenType.FUNCTION_NAME, functionName);
    }

    private void extractTagName() {
        int startIndex = currentIndex;

        while ((currentIndex < data.length) && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_' || data[currentIndex] == '=')) {
            currentIndex++;
            if (data[currentIndex - 1] == '=') break;
        }

        String tagName = new String(data, startIndex, currentIndex - startIndex);
        currentToken = new Token(TokenType.TAG_NAME, tagName.toUpperCase());
    }


    private void extractTagEnd() {
        currentIndex++;
        if (currentIndex == data.length) throw new SmartScriptLexerException("unable to tokenize the END_TAG token, missing }, end of data reached");
        skipBlanks();
        if (data[currentIndex++] != '}') throw new SmartScriptLexerException("unable to tokenize the END_TAG token, invalid char following the $, should be }");
        currentToken = new Token(TokenType.TAG_END, null);
    }


    private void extractTagStart() {
       currentIndex++;
       if (currentIndex == data.length) throw new SmartScriptLexerException("unable to tokenize the start tag token, missing $, end of data reached");
       skipBlanks();
       if (data[currentIndex++] != '$') throw new SmartScriptLexerException("unable to tokenize the start tag token, invalid char following the {, should be $");
       currentToken = new Token(TokenType.TAG_START, null);
    }

    private void extractText() {
        boolean escaping = false;

        ArrayList<Character> currentTextChars = new ArrayList<>();

        while (currentIndex < data.length) {
            if (escaping) {
                if (data[currentIndex] == '\\' || data[currentIndex] == '{') {
                    // current chars can go after \
                    currentTextChars.add(data[currentIndex]);
                    escaping = false;
                    currentIndex++;

                } else {
                    throw new SmartScriptLexerException("invalid character after escape char \\ in text");
                }

            } else {
                if (data[currentIndex] == '\\') {
                    escaping = true;
                    currentIndex++;
                } else if (data[currentIndex] == '{') {
                    try {
                        char nextNonBlackChar = nextNonBlankChar(currentIndex);
                        if (nextNonBlackChar == '$') break;
                    } catch (SmartScriptLexerException ignored) {}
                    currentTextChars.add(data[currentIndex]);
                    currentIndex++;
                } else {
                    currentTextChars.add(data[currentIndex]);
                    currentIndex++;
                }
            }
        }

        if (escaping) throw new SmartScriptLexerException("unmatched escaping char \\");

        StringBuilder sb = new StringBuilder();
        for (Character currentTextChar : currentTextChars) {
            sb.append(currentTextChar);
        }
        String text = sb.toString();
        currentToken = new Token(TokenType.TEXT, text);
    }

    private char nextNonBlankChar(int currentIndex) {
        int offset = 1;
        while (true) {
            if (currentIndex + offset == data.length) throw new SmartScriptLexerException();
            char c = data[currentIndex + offset];
            if (!isBlankChar(c)) return c;
            offset++;
        }
    }

    private boolean isBlankChar(char c) {
        return (c == ' ' || c == '\t' || c == '\n' || c== '\r');
    }

    private void skipBlanks() {
        while (currentIndex < data.length) {
            char c = data[currentIndex];
            if (isBlankChar(c)) {
                currentIndex++;
                continue;
            }
            break;
        }
    }
}
