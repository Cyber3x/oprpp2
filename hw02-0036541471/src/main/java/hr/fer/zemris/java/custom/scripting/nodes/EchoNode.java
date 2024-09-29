package hr.fer.zemris.java.custom.scripting.nodes;


import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EchoNode extends Node {
    private final Element[] elements;

    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("{$= ");
        for (Element element : elements) {
            if (element instanceof ElementString) {
                out.append("\"");
                out.append(element);
                out.append("\"");
            } else if (element instanceof ElementFunction) {
                out.append("@");
                out.append(element);
            } else {
                out.append(element);
            }
            out.append(" ");
        }
        out.append("$}");
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EchoNode echoNode = (EchoNode) o;
        return Arrays.equals(elements, echoNode.elements);
    }

    @Override
    public String debugPrint() {
        return "[ECHO NODE]: " + String.join(", ", Arrays.stream(elements).map(String::valueOf).collect(Collectors.joining(", ")));
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitEchoNode(this);
    }
}
