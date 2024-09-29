package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static hr.fer.zemris.java.webserver.Utils.getFileExtension;
import static hr.fer.zemris.java.webserver.Utils.isPathReadableFile;


public class ClientWorker implements Runnable, IDispatcher {
    private final Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String version;
    private String method;
    private String host;
    private Map<String, String> params = new HashMap<>();
    private final Map<String, String> temporaryParameters = new HashMap<>();
    private Map<String, String> permanentParameters = new HashMap<>();
    private final List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
    private String SID;

    private final Map<String, IWebWorker> webWorkers;
    private final Path documentRoot;
    private final Map<String, String> mimeTypes;
    private RequestContext context = null;

    private Map<String, String> requestHeaders;
    private final SmartHttpServer smartHttpServer;
    public ClientWorker(Socket clientSocket, String serverDomainName, Path documentRoot, Map<String, String> mimeTypes, Map<String, IWebWorker> webWorkers, SmartHttpServer smartHttpServer) {
        this.clientSocket = clientSocket;
        this.host = serverDomainName;
        this.webWorkers = webWorkers;
        this.documentRoot = documentRoot;
        this.mimeTypes = mimeTypes;
        this.smartHttpServer = smartHttpServer;
    }

    @Override
    public void run() {
        try {
            inputStream = new BufferedInputStream(clientSocket.getInputStream());
            outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            List<String> requestLines = readRequest(inputStream);

            if (requestLines.isEmpty()) {
                sendEmptyResponse(outputStream, 400, "Request invalid. Too small");
                return;
            }
            String firstLine = requestLines.getFirst();
            String[] firstLineParts = firstLine.split(" ");
            method = firstLineParts[0].toUpperCase();
            if (!method.equals("GET")) {
                System.out.println("Method Not Allowed: " + method);
                sendEmptyResponse(outputStream, 405, "Method Not Allowed");
                return;
            }

            version = firstLineParts[2].toUpperCase();
            if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
                System.out.println("HTTP Version Not Supported: " + version);
                sendEmptyResponse(outputStream, 505, "HTTP Version Not Supported");
                return;
            }

            requestHeaders = requestLines.stream().skip(1).map(line -> line.split(":")).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1].trim()));
            requestHeaders.putIfAbsent("Host", host);

            String fullRequestedPathname = firstLineParts[1];
            String[] requestedPathnameParts = fullRequestedPathname.split("\\?");
            String pathname, pathParams;
            pathname = requestedPathnameParts[0];
            System.out.println("incomingPath = " + pathname);

            if (requestedPathnameParts.length > 1) {
                pathParams = requestedPathnameParts[1];
                params = parsePathParams(pathParams);
            }

            internalDispatchRequest(pathname, true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException ignore) {
                }
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error while closing client socket: " + e.getMessage());
            }
            System.out.println("Request handled, thread shutting down.");
        }
    }

    private static Map<String, String> parsePathParams(String pathParams) {
        return Arrays.stream(pathParams.split("&")).map(pair -> pair.split("=")).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }

    private static List<String> readRequest(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int state = 0;
        label:
        while (true) {
            int currentByte = inputStream.read();
            if (currentByte == -1) {
                if (bos.size() != 0) {
                    throw new IOException("Incomplete header received.");
                }
                return List.of();
            }
            if (currentByte != 13) {
                bos.write(currentByte);
            }

            switch (state) {
                case 0:
                    if (currentByte == 13) {
                        state = 1;
                    } else if (currentByte == 10) {
                        state = 4;
                    }
                    break;
                case 1:
                    state = currentByte == 10 ? 2 : 0;
                    break;
                case 2:
                    state = currentByte == 13 ? 3 : 0;
                    break;
                case 3, 4:
                    if (currentByte == 10) {
                        break label;
                    } else {
                        state = 0;
                    }
                    break;
            }
        }
        return List.of(bos.toString(StandardCharsets.ISO_8859_1).split("\n"));
    }

    public static void sendEmptyResponse(OutputStream outputStream, int statusCode, String statusText) throws IOException {
        outputStream.write(
                (
                        String.format("HTTP/1.1 %d %s \r\n", statusCode, statusText) +
                                "Server: simple java server\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes(StandardCharsets.ISO_8859_1)
        );
        outputStream.flush();
    }

    @Override
    public void dispatchRequest(String urlPath) throws Exception {
        internalDispatchRequest(urlPath, false);
    }

    private RequestContext getContextOrCreate(RequestContext newRequestContext) {
        if (context == null) context = newRequestContext;
        return context;
    }

    private IWebWorker getWebWorker(String workerClassName) {

        // does the worker exist?
        Path workerClassPath = Paths.get("src/main/java/hr/fer/zemris/java/webserver/workers/" + workerClassName + ".java");
        System.out.println("Testing if worker exists at: " + workerClassPath);
        if (!isPathReadableFile(workerClassPath)) {
            System.out.println("A worker with this workerClassName is not found!");
            return null;
        };

        // create worker
        String FQCN = "hr.fer.zemris.java.webserver.workers." + workerClassName;

        try {
            Class<?> referenceToClass = Class.forName(FQCN);
            return (IWebWorker) referenceToClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
        Path requestedPath = documentRoot.resolve(Path.of(urlPath.substring(1)));

        if (!requestedPath.normalize().startsWith(documentRoot)) {
            // if the normalized path doesn't start with te document root path than it's not a sub-path, thus it's forbidden
            sendEmptyResponse(outputStream, 403, "Forbidden");
            return;
        }
        System.out.println("serverPath = " + requestedPath);

        if (directCall && urlPath.startsWith("/private")) {
            sendEmptyResponse(outputStream, 403, "Not Allowed");
            return;
        }


        Utils.Pair<String, Map<String, String>> sessionEntryInfo = smartHttpServer.checkSession(requestHeaders);
        permanentParameters = sessionEntryInfo.value2();
        SID = sessionEntryInfo.value1();

        RequestContext defaultRequestContext = new RequestContext(outputStream, params, permanentParameters, outputCookies, temporaryParameters, this, SID);
        defaultRequestContext.addRCCookie(new RequestContext.RCCookie(
                "sid",
                SID,
                null,
                requestHeaders.get("Host"),
                "/",
                true
        ));

        IWebWorker webWorker = null;

        String[] urlParts = urlPath.substring(1).split("/");
        if (urlParts.length == 2 && urlParts[0].equals("ext")) {
            // summon web worker
            webWorker = getWebWorker(urlParts[1]);
        } else if (webWorkers.containsKey(urlPath)) {
            // get already summoned web worker
            webWorker = webWorkers.get(urlPath);
            System.out.println("Found mapped web worker = " + webWorker.getClass().getSimpleName());
        }

        if (webWorker != null) {
            System.out.println("This route is registered to a web worker: " + webWorker.getClass().getSimpleName());
            webWorker.processRequest(getContextOrCreate(defaultRequestContext));
            return;
        }

        if (!isPathReadableFile(requestedPath)) {
            sendEmptyResponse(outputStream, 404, "File not found");
            return;
        }

        String fileExtension = getFileExtension(requestedPath);

        if (fileExtension != null && fileExtension.equals("smscr")) {
            System.out.println("Calling the smartScriptEngine");
            new SmartScriptEngine(
                    new SmartScriptParser(Files.readString(requestedPath)).getDocumentNode(),
                    getContextOrCreate(defaultRequestContext)
            ).execute();
        } else {
            String mimeType = mimeTypes.get(fileExtension);

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            getContextOrCreate(defaultRequestContext).setMimeType(mimeType);
            getContextOrCreate(defaultRequestContext).setStatusCode(200);
            getContextOrCreate(defaultRequestContext).setContentLength(Files.size(requestedPath));

            try (InputStream is = Files.newInputStream(requestedPath)) {
                getContextOrCreate(defaultRequestContext).write(is.readAllBytes());
            }
        }
    }
}