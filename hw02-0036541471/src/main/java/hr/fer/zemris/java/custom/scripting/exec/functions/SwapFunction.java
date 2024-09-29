package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine.TEMPORARY_STACK;

public class SwapFunction implements SmartScriptFunction {
    @Override
    public void execute(ObjectMultistack multistack, RequestContext requestContext) {
        ValueWrapper a = multistack.pop(TEMPORARY_STACK);
        ValueWrapper b = multistack.pop(TEMPORARY_STACK);
        multistack.push(TEMPORARY_STACK, a);
        multistack.push(TEMPORARY_STACK, b);
    }
}
