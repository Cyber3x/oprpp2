package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TreeWriter {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("needs filename");
            System.exit(1);
        }

        String docBody = Files.readString(Path.of(args[0]));
        SmartScriptParser parser = new SmartScriptParser(docBody);
        WriterVisitor visitor = new WriterVisitor();

        parser.getDocumentNode().accept(visitor);

    }


    static class WriterVisitor implements INodeVisitor {
        @Override
        public void visitTextNode(TextNode node) {
            System.out.print(node);
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            System.out.print(node);
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
            System.out.print("{$END$}");
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            System.out.print(node);
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }
    }
}
