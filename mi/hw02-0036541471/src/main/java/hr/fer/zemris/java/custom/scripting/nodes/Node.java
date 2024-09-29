package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public abstract class Node {
    private ArrayList<Node> childrenNodes;


    public void addChildNode(Node child) {
        if (childrenNodes == null) {
            childrenNodes = new ArrayList<>();
        }
        childrenNodes.add(child);
    }

    public int numberOfChildren() {
        if (childrenNodes == null) {
            return 0;
        }
        return childrenNodes.size();
    }

    /**
     * get the node at the index
     * @param index target index
     * @return target child node at the target index
     * @throws NoSuchElementException if there's no children, and we're requesting a child
     * @throws IndexOutOfBoundsException if we're requesting a child at an invalid index
     */
    public Node getChild(int index) {
        if (childrenNodes == null) throw new NoSuchElementException("This node doesn't have any children nodes");
        return (Node) this.childrenNodes.get(index);
    }

    @Override
    public String toString() {
        return getString(true);
    }


    public String getString(boolean skip) {
        StringBuilder sb = new StringBuilder();
        if (!skip) sb.append(this);
        if (this.childrenNodes != null) {
            for (int i = 0; i < numberOfChildren(); i++) {
                Node child = getChild(i);
                sb.append(child.getString(false));
                if (child instanceof ForLoopNode) sb.append("\n{$ END $}");
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return childrenNodes.equals(node.childrenNodes);
    }

    public void debugPrint(int depth) {
        System.out.println(" ".repeat(depth * 3) + this.debugPrint());
        if (this.childrenNodes != null) {
            for (int i = 0; i < numberOfChildren(); i++) {
                getChild(i).debugPrint(depth + 1);
            }
        }
    }

    public String debugPrint() {
        return "[BASE NODE]";
    }

    public abstract void accept(INodeVisitor visitor);
}
