package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine.TEMPORARY_STACK;

public class ParamGetFunction implements SmartScriptFunction{
    @Override
    public void execute(ObjectMultistack multistack, RequestContext requestContext) {
        ValueWrapper defaultValue = multistack.pop(TEMPORARY_STACK);
        ValueWrapper paramName = multistack.pop(TEMPORARY_STACK);
        String paramValue = requestContext.getParameter(paramName.getValue().toString());
        multistack.push(TEMPORARY_STACK, paramValue == null ? defaultValue : new ValueWrapper(paramValue));
    }
}
