package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine.TEMPORARY_STACK;

public class PersistentParamDelFunction implements SmartScriptFunction{
    @Override
    public void execute(ObjectMultistack multistack, RequestContext requestContext) {
        ValueWrapper name = multistack.pop(TEMPORARY_STACK);
        requestContext.removePersistentParameter(name.getValue().toString());
    }
}
