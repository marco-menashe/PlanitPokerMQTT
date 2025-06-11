package com.example;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Sends structured log events to an HTTP endpoint.
 */
public class HttpAppender extends AppenderBase<ILoggingEvent> {
    private String url;
    private String bearerToken;

    @Override
    protected void append(ILoggingEvent event) {
        try {
            JSONObject json = new JSONObject();
            json.put("level", event.getLevel().toString());
            json.put("loggerName", event.getLoggerName());
            json.put("message", event.getFormattedMessage());
            json.put("threadName", event.getThreadName());
            json.put("timestamp", event.getTimeStamp());
            sendLog(json.toString());
        } catch (Exception e) {
            addError("Failed to send log", e);
        }
    }

    private void sendLog(String jsonLog) throws Exception {
        URL endpoint = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + this.bearerToken);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] payload = jsonLog.getBytes(StandardCharsets.UTF_8);
            os.write(payload, 0, payload.length);
        }

        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Log HTTP error: " + code);
        }
    }

    // setters used by Logback configuration
    public void setUrl(String url) {
        this.url = url;
    }
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
