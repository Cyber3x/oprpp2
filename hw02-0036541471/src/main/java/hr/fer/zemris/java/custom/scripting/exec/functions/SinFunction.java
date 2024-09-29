package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine.TEMPORARY_STACK;

public class SinFunction implements SmartScriptFunction {
    @Override
    public void execute(ObjectMultistack multistack, RequestContext requestContext) {
        Object x = multistack.pop(TEMPORARY_STACK).getValue();
        Double r = Math.sin(Math.toRadians((x instanceof Double) ? (Double) x : ((Integer) x).doubleValue()));
        multistack.push(TEMPORARY_STACK, new ValueWrapper(r));
    }
}
