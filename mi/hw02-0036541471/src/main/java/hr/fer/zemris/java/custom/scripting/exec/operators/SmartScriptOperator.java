package hr.fer.zemris.java.custom.scripting.exec.operators;

import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

public interface SmartScriptOperator {
    void apply(ValueWrapper first, ValueWrapper second);
}
