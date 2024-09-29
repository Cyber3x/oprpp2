package hr.fer.zemris.java.custom.scripting.nodes;

public class TextNode extends Node {
    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return this.text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextNode textNode = (TextNode) o;
        return text.equals(textNode.text);
    }

    @Override
    public String debugPrint() {
        return "[TEXT NODE]: " + this.text.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitTextNode(this);
    }
}
