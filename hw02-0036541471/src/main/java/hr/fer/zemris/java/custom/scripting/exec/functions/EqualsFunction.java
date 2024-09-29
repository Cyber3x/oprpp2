package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine.TEMPORARY_STACK;

public class EqualsFunction implements SmartScriptFunction{
    @Override
    public void execute(ObjectMultistack multistack, RequestContext requestContext) {
        ValueWrapper ifFalse = multistack.pop(TEMPORARY_STACK);
        ValueWrapper ifTrue = multistack.pop(TEMPORARY_STACK);
        ValueWrapper first = multistack.pop(TEMPORARY_STACK);
        ValueWrapper second = multistack.pop(TEMPORARY_STACK);
        if (first.toString().equalsIgnoreCase(second.toString())) {
            multistack.push(TEMPORARY_STACK, ifTrue);
        } else {
            multistack.push(TEMPORARY_STACK, ifFalse);
        }
    }
}
