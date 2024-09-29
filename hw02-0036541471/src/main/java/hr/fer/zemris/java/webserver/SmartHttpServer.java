package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class SmartHttpServer {
    private static final int SERVER_SOCKET_TIMEOUT_MS = 10_000;
    private String address;
    private String domainName;
    private int port;
    private int workerThreads;
    private int sessionTimeout;
    private final Map<String, String> mimeTypes = new HashMap<>();
    private ServerThread serverThread;
    private ExecutorService threadPool;
    private Path documentRoot;

    private final ServerSocket serverSocket;
    private final Map<String, IWebWorker> workerMap = new HashMap<>();

    private final Map<String, SessionMapEntry> sessions = new HashMap<>();
    private final Random sessionRandom = new Random();


    public static void main(String[] args) {
        SmartHttpServer httpServer = new SmartHttpServer(args[0]);
        httpServer.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals("stop")) {
                httpServer.stop();
                break;
            }
        }
    }

    public SmartHttpServer(String configFileName) {
        readConfigFiles(configFileName);
        serverSocket = createServerSocket();
        try (ScheduledExecutorService expiredSessionsCollector = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        })) {
            expiredSessionsCollector.scheduleAtFixedRate(() -> {
                sessions.forEach((s, sessionMapEntry) -> {
                    if (new Date().getTime() > sessionMapEntry.validUntil) sessions.remove(s);
                });
            }, 0, 5, TimeUnit.MINUTES);
        }
    }


    protected synchronized void start() {
        if (serverThread != null) {
            System.out.println("Server already running!");
            return;
        }
        serverThread = new ServerThread();
        serverThread.start();
        System.out.println("Server started at -> " + address + ":" + port);

        threadPool = Executors.newFixedThreadPool(workerThreads);
    }

    protected synchronized void stop() {
        System.out.println("Server shutting down...");
        serverThread.stopThead();

        threadPool.shutdown();
    }

    protected class ServerThread extends Thread {
        private boolean running = true;

        public void stopThead() {
            System.out.println("Stopping server thread...");
            running = false;
        }

        @Override
        public void run() {
            Socket clientSocket;
            while (running) {
                try {
                    clientSocket = serverSocket.accept();
                } catch (SocketTimeoutException ste) {
                    continue;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("\n\nAccepted new connection from: " + clientSocket.getRemoteSocketAddress() + ". Creating new thread");
                threadPool.submit(new ClientWorker(clientSocket, domainName, documentRoot, mimeTypes, workerMap, SmartHttpServer.this));
            }
            System.out.println("Server thread stopped.");
        }
    }

    private static class SessionMapEntry {
        private final String sid;
        private final String host;
        private long validUntil;
        Map<String, String> map;

        public SessionMapEntry(String sid, String host, long validUntil) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            map = new ConcurrentHashMap<>();
        }
    }

    public synchronized Utils.Pair<String, Map<String, String>> checkSession(Map<String, String> headers) {
        String sidCandidate = null;

        outer:
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (!entry.getKey().equals("Cookie")) continue;

            String[] cookiePairs =entry.getValue().split(";");
            for (String pair : cookiePairs) {
                if (pair.startsWith("sid")) {
                    sidCandidate = pair.split("=")[1].replaceAll("\"", "");
                    break outer;
                }
            }
        }

        if (sidCandidate == null) {
            // There's no sid value in the cookies
            SessionMapEntry newMapEntry = createNewSessionEntry(headers.get("Host"));
            return new Utils.Pair<>(newMapEntry.sid, newMapEntry.map);
        }

        SessionMapEntry foundSessionEntry = sessions.get(sidCandidate);
        if (foundSessionEntry == null || !foundSessionEntry.host.equals(headers.get("Host"))) {
            // The host from the entry doesn't match the host from the request
            SessionMapEntry newMapEntry = createNewSessionEntry(headers.get("Host"));
            return new Utils.Pair<>(newMapEntry.sid, newMapEntry.map);
        }

        if (new Date().getTime() > foundSessionEntry.validUntil) {
            // the entry if too old
            sessions.remove(foundSessionEntry.sid);
            SessionMapEntry newMapEntry = createNewSessionEntry(headers.get("Host"));
            return new Utils.Pair<>(newMapEntry.sid, newMapEntry.map);
        }

        // the entry is valid
        foundSessionEntry.validUntil = new Date().getTime() + sessionTimeout * 1000L;
        return new Utils.Pair<>(foundSessionEntry.sid, foundSessionEntry.map);
    }

    private synchronized SessionMapEntry createNewSessionEntry(String host) {
        String newSID = generateSID(20);
        SessionMapEntry mapEntry = new SessionMapEntry(
                newSID,
                host,
                new Date().getTime() + sessionTimeout * 1000L
        );
        sessions.put(newSID, mapEntry);
        return mapEntry;
    }

    private synchronized String generateSID(int length) {
        int leftLimit = 97; // a
        int rightLimit = 122; // z

        return sessionRandom.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();
    }

    private void readConfigFiles(String configFileName) {
        Properties serverProperties = new Properties();
        try {
            serverProperties.load(Files.newInputStream(Path.of(configFileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        address = serverProperties.getProperty("server.address");
        domainName = serverProperties.getProperty("server.domainName");
        port = Integer.parseInt(serverProperties.getProperty("server.port"));
        workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
        sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));
        documentRoot = Path.of(serverProperties.getProperty("server.documentRoot"));

        // load mime types
        Path mimeTypesPath = Path.of(serverProperties.getProperty("server.mimeConfig"));
        Properties mimeProperties = new Properties();
        try {
            mimeProperties.load(Files.newInputStream(mimeTypesPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mimeProperties.forEach((key, value) -> mimeTypes.put(key.toString(), value.toString()));

        // load web workers
        Path webWorkersPath = Path.of(serverProperties.getProperty("server.workers"));
        Properties webWorkerProperties = new Properties();
        try {
            webWorkerProperties.load(Files.newInputStream(webWorkersPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        webWorkerProperties.forEach((path, FQCN) -> {
            if (workerMap.containsKey(path.toString()))
                throw new RuntimeException("a worker for path: " + path + " was already declared.");

            try {
                Class<?> referenceToClass = Class.forName(FQCN.toString());
                IWebWorker webWorker = (IWebWorker) referenceToClass.getConstructor().newInstance();
                workerMap.put((String) path, webWorker);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private ServerSocket createServerSocket() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT_MS);
            return serverSocket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDomainName() {
        return domainName;
    }

    public Map<String, String> getMimeTypes() {
        return mimeTypes;
    }

    public Path getDocumentRoot() {
        return documentRoot;
    }

    public Map<String, IWebWorker> getWorkerMap() {
        return workerMap;
    }
}
