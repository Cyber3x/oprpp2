package hr.fer.zemris.java.custom.scripting.demo.engine;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zbrajanje {
    public static void main(String[] args) throws IOException {
        String documentBody = Files.readString(Path.of("scripts/zbrajanje.smscr"));

        Map<String, String> parameter = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        parameter.put("a", "4");
        parameter.put("b", "2");

        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameter, persistentParameters, cookies)
        ).execute();
    }
}
