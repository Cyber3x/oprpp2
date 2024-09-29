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

public class BrojPoziva {
    public static void main(String[] args) throws IOException {
        String docBody = Files.readString(Path.of("scripts/brojPoziva.smscr"));
        Map<String, String> parameter = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();

        persistentParameters.put("brojPoziva", "3");

        RequestContext requestContext = new RequestContext(System.out, parameter, persistentParameters, cookies);
        new SmartScriptEngine(
                new SmartScriptParser(docBody).getDocumentNode(),
                requestContext
        ).execute();

        System.out.println("vrijednost u mapi: " + requestContext.getPersistentParameter("brojPoziva"));
    }
}
