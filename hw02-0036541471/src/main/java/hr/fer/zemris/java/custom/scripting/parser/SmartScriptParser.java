package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class SmartScriptParser {

    private final Stack<Node> stack = new Stack<>();
    private final SmartScriptLexer lexer;


    /**
     * Creates a new parser and parses the given documentBody
     * @param documentBody string which is your input code
     */
    public SmartScriptParser(String documentBody) {
        lexer = new SmartScriptLexer(documentBody);
        stack.push(new DocumentNode());
        parse();
    }

    private void parse() {
        Token currentToken;

        do {
            currentToken = lexer.nextToken();
            switch (currentToken.getType()) {
                case EOF -> {
                    return;
                }
                case TEXT -> addChildToLastNodeOnStack(new TextNode((String) currentToken.getValue()));
                case TAG_START -> {
                    currentToken = lexer.nextToken();
                    switch (String.valueOf(currentToken.getValue())) {
                        case "=" -> createEchoNode();
                        case "FOR" -> createForNode();
                        case "END" -> {
                            currentToken = lexer.nextToken();
                            throwIfNotAllowed(currentToken, "END tag name must be followed by a closing tag token", TokenType.TAG_END);
                            stack.pop();
                        }
                        default -> createCustomNode();

                        // custom tag name
                    }
                }
                default -> throw new SmartScriptParserException("unsupported token type " + currentToken);
            }
        } while (currentToken.getType() != TokenType.EOF);
    }

    /**
     * Returns the root node of the parsed tree
     * @return Root node of the parsed code
     */
    public DocumentNode getDocumentNode() {
        if (stack.size() != 1) throw new SmartScriptParserException("the stack is not only the document node");
        return (DocumentNode) stack.peek();
    }

    private void createForNode() {
        Token currentToken;

        currentToken = lexer.nextToken();

        // first element can only be a variable
        if (currentToken.getType() != TokenType.VARIABLE_NAME)
            throw new SmartScriptParserException("first element of for tag must be a variable name");
        ElementVariable variable = new ElementVariable(currentToken.getValue().toString());

        currentToken = lexer.nextToken();
        // second element can be var name or number or string
        throwIfNotAllowed(currentToken, "Unsupported second token while build for node.",
                TokenType.VARIABLE_NAME, TokenType.NUMBER, TokenType.STRING);

        Element startExpression = parseForNodeElement(currentToken);


        currentToken = lexer.nextToken();
        // third element can be var name or number or string
        throwIfNotAllowed(currentToken, "Unsupported third token while building for node.",
                TokenType.VARIABLE_NAME, TokenType.NUMBER, TokenType.STRING);

        Element endExpression = parseForNodeElement(currentToken);


        currentToken = lexer.nextToken();
        // fourth element is optional and can be var name or number or string
        throwIfNotAllowed(currentToken, "Unsupported fourth token while building for node.",
                TokenType.VARIABLE_NAME, TokenType.NUMBER, TokenType.STRING, TokenType.TAG_END);
        ForLoopNode forLoopNode;

        if (currentToken.getType() == TokenType.TAG_END) {
            // there was no step expression
            forLoopNode = new ForLoopNode(variable, startExpression, endExpression);

        } else {
            // there was a step expression
            Element stepExpression = parseForNodeElement(currentToken);
            forLoopNode = new ForLoopNode(variable, startExpression, endExpression, stepExpression);

            currentToken = lexer.nextToken();
            throwIfNotAllowed(currentToken, "the fifth element must be a closing tag.", TokenType.TAG_END);
        }

        // add as child to last thing on stack and put it on the stack
        addChildToLastNodeOnStack(forLoopNode);
        stack.push(forLoopNode);
    }

    private Element parseForNodeElement(Token token) {
        return switch (token.getType()) {
            case VARIABLE_NAME -> new ElementVariable(token.getValue().toString());
            case NUMBER -> parseElementNumber(token.getValue().toString());
            case STRING -> new ElementString(token.getValue().toString());
            default -> throw new SmartScriptParserException("parse for node element wrong branch");
        };
    }

    private void throwIfNotAllowed(Token currentToken, String throwMessage, TokenType... tokenTypes) {
        if (Arrays.stream(tokenTypes).noneMatch((tokenType) -> currentToken.getType() == tokenType))
            throw new SmartScriptParserException(throwMessage + " " + currentToken);
    }


    private void createCustomNode() {
        throw new SmartScriptParserException("parsing for custom tags not implemented yet. " + lexer.getToken());
    }

    private void createEchoNode() {
        Token currentToken;

        ArrayList<Element> echoNodeElementsArray = new ArrayList<>();

        do {
            currentToken = lexer.nextToken();
            switch (currentToken.getType()) {
                case EOF -> throw new SmartScriptParserException("unable to build echo token, EOF reached");
                case VARIABLE_NAME -> echoNodeElementsArray.add(new ElementVariable(currentToken.getValue().toString()));
                case STRING -> {
                    ElementString elementString = new ElementString(currentToken.getValue().toString());
                    echoNodeElementsArray.add(elementString);
                }
                case OPERATOR -> {
                    ElementOperator elementOperator = new ElementOperator(currentToken.getValue().toString());
                    echoNodeElementsArray.add(elementOperator);
                }
                case FUNCTION_NAME -> {
                    ElementFunction elementFunction = new ElementFunction(currentToken.getValue().toString());
                    echoNodeElementsArray.add(elementFunction);
                }
                case NUMBER -> {
                    String numberString = currentToken.getValue().toString();
                    echoNodeElementsArray.add(parseElementNumber(numberString));
                }
                case TAG_END -> {
                }
                default -> throw new SmartScriptLexerException("unable to parse token into echo node element, token: "
                        + currentToken.getType() + " of value: " + currentToken.getValue());
            }
        } while (currentToken.getType() != TokenType.TAG_END);

        EchoNode echoNode = new EchoNode(echoNodeElementsArray.toArray(new Element[]{}));

        addChildToLastNodeOnStack(echoNode);
    }

    private Element parseElementNumber(String number) {
        try {
            if (number.contains(".")) {
                double parseDouble = Double.parseDouble(number);
                return new ElementConstantDouble(parseDouble);
            } else {
                int parseInt = Integer.parseInt(number);
                return new ElementConstantInteger(parseInt);
            }
        } catch (NumberFormatException exception) {
            throw new SmartScriptParserException("Unable to parse number");
        }
    }


    private void addChildToLastNodeOnStack(Node newChild) {
        Node lastStackNode = stack.peek();
        lastStackNode.addChildNode(newChild);
    }
}
