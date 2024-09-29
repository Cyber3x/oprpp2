package hr.fer.zemris.java.custom.scripting.nodes;

public class DocumentNode extends Node {
    @Override
    public String debugPrint() {
        return "[DOCUMENT NODE]";
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitDocumentNode(this);
    }
}
