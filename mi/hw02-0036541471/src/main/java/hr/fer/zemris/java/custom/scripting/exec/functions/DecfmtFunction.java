package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.text.DecimalFormat;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine.TEMPORARY_STACK;


public class DecfmtFunction implements SmartScriptFunction {
    @Override
    public void execute(ObjectMultistack multistack, RequestContext requestContext) {
        ValueWrapper f = multistack.pop(TEMPORARY_STACK);
        ValueWrapper v = multistack.pop(TEMPORARY_STACK);
        DecimalFormat decimalFormat = new DecimalFormat((String) f.getValue());
        String r = decimalFormat.format(v.getValue());
        multistack.push(TEMPORARY_STACK, new ValueWrapper(r));
    }
}
