package hr.fer.zemris.java.custom.scripting.nodes;


import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

import java.util.Objects;

public class ForLoopNode extends Node {
    private final ElementVariable variable;
    private final Element startExpression;
    private final Element endExpression;

    // can be null
    private final Element stepExpression;

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
        this(variable, startExpression, endExpression, null);
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public String toString() {
        if (stepExpression != null) {
            return String.format("{$ FOR %s %s %s %s $}", variable, startExpression, endExpression, stepExpression);
        }  else {
            return String.format("{$ FOR %s %s %s $}", variable, startExpression, endExpression);
        }
    }

    @Override
    public String debugPrint() {
        if (stepExpression != null) {
            return String.format("[FOR LOOP NODE]: var: %s, start: %s, end: %s, step: %s", variable, startExpression, endExpression, stepExpression);
        }  else {
            return String.format("[FOR LOOP NODE]: var: %s, start: %s, end: %s", variable, startExpression, endExpression);
        }
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForLoopNode that = (ForLoopNode) o;
        return variable.equals(that.variable) && startExpression.equals(that.startExpression) && endExpression.equals(that.endExpression) && Objects.equals(stepExpression, that.stepExpression);
    }

}
