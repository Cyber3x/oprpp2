package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestContext {
    // write-only
    private String encoding = "UTF-8";
    private int statusCode = 200;
    private String statusText = "OK";
    private String mimeType = "text/html";
    private Long contentLength = null;


    // private
    private final OutputStream outputStream;
    private Charset charset;
    private boolean headerGenerated = false;
    private final Map<String, String> parameters;
    private Map<String, String> temporaryParameters;
    private Map<String, String> persistentParameters;
    private final List<RCCookie> outputCookies;

    private final IDispatcher dispatcher;

    private final String SID;


    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
    }

    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies, Map<String, String> temporaryParameters, IDispatcher dispatcher, String sid) {
        this.outputStream = outputStream;
        this.parameters = parameters;
        this.persistentParameters = persistentParameters;
        this.outputCookies = outputCookies;
        this.temporaryParameters = temporaryParameters;
        this.dispatcher = dispatcher;
        SID = sid;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    public String getSessionID() {
        return SID;
    }

    public void setTemporaryParameter(String name, String value) {
        temporaryParameters.put(name, value);
    }

    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    public void addRCCookie(RCCookie cookie) {
        outputCookies.add(cookie);
    }

    public RequestContext write(byte[] data) throws IOException {
        if (!headerGenerated) constructHeader();
        outputStream.write(data);
        return this;
    }

    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        if (!headerGenerated) constructHeader();
        byte[] selectedData = new byte[len];
        System.arraycopy(data, offset, selectedData, 0, len);
        return write(selectedData);
    }

    public RequestContext write(String data) throws IOException {
        if (!headerGenerated) constructHeader();
        return write(data.getBytes(charset));
    }

    public void setEncoding(String encoding) {
        if (headerGenerated) throw new RuntimeException();
        this.encoding = encoding;
    }

    public void setStatusCode(int statusCode) {
        if (headerGenerated) throw new RuntimeException();
        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) {
        if (headerGenerated) throw new RuntimeException();
        this.statusText = statusText;
    }

    public void setMimeType(String mimeType) {
        if (headerGenerated) throw new RuntimeException();
        this.mimeType = mimeType;
    }

    public void setContentLength(long contentLength) {
        if (headerGenerated) throw new RuntimeException();
        this.contentLength = contentLength;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getTemporaryParameters() {
        return temporaryParameters;
    }

    public void setTemporaryParameters(Map<String, String> temporaryParameters) {
        this.temporaryParameters = temporaryParameters;
    }

    public Map<String, String> getPersistentParameters() {
        return persistentParameters;
    }

    public void setPersistentParameters(Map<String, String> persistentParameters) {
        this.persistentParameters = persistentParameters;
    }

    private void constructHeader() throws IOException {
        charset = Charset.forName(encoding);
        headerGenerated = true;

        ArrayList<String> headerLines = new ArrayList<>();

        headerLines.add(String.format("HTTP/1.1 %s %s", statusCode, statusText));

        String contentType = String.format("Content-Type: %s", mimeType);
        if (mimeType.startsWith("text/")) contentType = contentType.concat("; charset=" + encoding);
        headerLines.add(contentType);

        if (contentLength != null) {
            headerLines.add("Content-Length: " + contentLength);
        }

        if (!outputCookies.isEmpty()) {
            for (RCCookie outputCookie : outputCookies) {
                String cookieInfoLine = getCookieInfoLine(outputCookie);
                headerLines.add(cookieInfoLine);
            }
        }

        headerLines.add("\r\n");

        String header = String.join("\r\n", headerLines);

        outputStream.write(header.getBytes(StandardCharsets.ISO_8859_1));
    }

    public void dispatch(String urlPath) {
        try {
            dispatcher.dispatchRequest(urlPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getCookieInfoLine(RCCookie outputCookie) {
        String cookieInfoLine = String.format("Set-Cookie: %s=\"%s\"", outputCookie.name, outputCookie.value);
        if (outputCookie.domain != null) {
            cookieInfoLine = cookieInfoLine.concat("; Domain=" + outputCookie.domain);
        }

        if (outputCookie.path != null) {
            cookieInfoLine = cookieInfoLine.concat("; Path=" + outputCookie.path);
        }

        if (outputCookie.maxAge != null) {
            cookieInfoLine = cookieInfoLine.concat("; Max-Age=" + outputCookie.maxAge);
        }

        if (outputCookie.httpOnly) {
            cookieInfoLine = cookieInfoLine.concat("; HttpOnly");
        }
        return cookieInfoLine;
    }

    public record RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
    }
}
