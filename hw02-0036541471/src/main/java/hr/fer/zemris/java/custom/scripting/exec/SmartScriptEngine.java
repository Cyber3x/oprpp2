package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.exec.functions.*;
import hr.fer.zemris.java.custom.scripting.exec.operators.SmartScriptOperator;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class SmartScriptEngine {
    public final static String OBJECT_STACK = "OBJECT_STACK";
    public final static String TEMPORARY_STACK = "TEMPORARY_STACK";
    private final DocumentNode documentNode;
    private final RequestContext requestContext;
    private final ObjectMultistack multistack = new ObjectMultistack();


    private final INodeVisitor visitor = new INodeVisitor() {

        private static final HashMap<String, SmartScriptFunction> functionMap = new HashMap<>();
        private static final HashMap<String, SmartScriptOperator> operatorsMap = new HashMap<>();

        static {
            functionMap.put("sin", new SinFunction());
            functionMap.put("decfmt", new DecfmtFunction());
            functionMap.put("setMimeType", new SetMimeTypeFunction());
            functionMap.put("dup", new DupFunction());
            functionMap.put("swap", new SwapFunction());
            functionMap.put("paramGet", new ParamGetFunction());
            functionMap.put("pparamGet", new PersistentParamGetFunction());
            functionMap.put("pparamSet", new PersistentParamSetFunction());
            functionMap.put("pparamDel", new PersistentParamDelFunction());
            functionMap.put("tparamGet", new TemporaryParamGetFunction());
            functionMap.put("tparamSet", new TemporaryParamSetFunction());
            functionMap.put("tparamDel", new TemporaryParamDelFunction());
            functionMap.put("equals", new EqualsFunction());

            operatorsMap.put("+", (first, second) -> first.add(second.getValue()));
            operatorsMap.put("-", (first, second) -> first.subtract(second.getValue()));
            operatorsMap.put("*", (first, second) -> first.multiply(second.getValue()));
            operatorsMap.put("/", (first, second) -> first.divide(second.getValue()));
        }

        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            multistack.push(OBJECT_STACK, new ValueWrapper(node.getStartExpression().asText()));

            for (
                    ;
                    multistack.peek(OBJECT_STACK).numCompare(node.getEndExpression().asText()) <= 0;
                    multistack.peek(OBJECT_STACK).add(node.getStepExpression().asText())
            ) {
                for (int i = 0; i < node.numberOfChildren(); i++) {
                    node.getChild(i).accept(this);
                }
            }

            multistack.pop(OBJECT_STACK);
        }

        @Override
        public void visitEchoNode(EchoNode node) {


            for (Element element : node.getElements()) {
                if (
                        (element instanceof ElementConstantDouble) ||
                                (element instanceof ElementConstantInteger) ||
                                (element instanceof ElementString)
                ) {
                    multistack.push(TEMPORARY_STACK, new ValueWrapper(element.asText()));

                } else if (element instanceof ElementVariable) {
                    ValueWrapper stackTop = multistack.peek(OBJECT_STACK);
                    multistack.push(TEMPORARY_STACK, new ValueWrapper(stackTop.getValue()));

                } else if (element instanceof ElementOperator elementOperator) {
                    ValueWrapper first = multistack.pop(TEMPORARY_STACK);
                    ValueWrapper second = multistack.pop(TEMPORARY_STACK);

                    String operatorSymbol = elementOperator.getSymbol();
                    SmartScriptOperator operator = operatorsMap.get(operatorSymbol);

                    if (operator == null) throw new RuntimeException("the operator: " + operatorSymbol + " is currently not supported.");

                    operator.apply(first, second);

                    multistack.push(TEMPORARY_STACK, first);

                } else if (element instanceof ElementFunction elementFunction) {
                    String functionName = elementFunction.getName();

                    SmartScriptFunction function = functionMap.get(functionName);

                    if (function == null)
                        throw new RuntimeException("the function: " + functionName + " is currently not supported.");

                    function.execute(multistack, requestContext);
                } else {
                    throw new RuntimeException("unknown element type got: " + element.getClass().getName());
                }
            }

            // write elements from the temporary stack to the request context, from bottom to top.
            String REVERSE_STACK = "REVERSE_STACK";

            while (true) {
                try {
                    ValueWrapper popped = multistack.pop(TEMPORARY_STACK);
                    multistack.push(REVERSE_STACK, popped);
                } catch (NoSuchElementException ignored) {
                    break;
                }
            }

            while (true) {
                try {
                    ValueWrapper popped = multistack.pop(REVERSE_STACK);
                    requestContext.write(popped.getValue().toString());
                } catch (NoSuchElementException ignored) {
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }
    };

    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;
    }

    public void execute() {
        documentNode.accept(visitor);
    }
}
