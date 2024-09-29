package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.webserver.RequestContext;

public interface SmartScriptFunction {
    void execute(ObjectMultistack multistack, RequestContext requestContext);
}
