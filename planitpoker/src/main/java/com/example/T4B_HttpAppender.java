package com.example;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Sends structured log events to an HTTP endpoint.
 *
 * @author Aidan
 */

public class T4B_HttpAppender extends AppenderBase<ILoggingEvent> {
    private String url;
    private String bearerToken;
    private static final Logger logger = LoggerFactory.getLogger(T4B_CreateRoomNanny.class);

    @Override
    protected void append(ILoggingEvent event) {
        try {
            JSONObject json = new JSONObject();
            json.put("level", event.getLevel().toString());
            json.put("loggerName", event.getLoggerName());
            json.put("message", event.getFormattedMessage());
            json.put("threadName", event.getThreadName());
            json.put("timestamp", event.getTimeStamp());
            logger.debug("HttpAppender: built JSON payload ({} bytes)", json.toString().length());
            sendLog(json.toString());
            logger.debug("HttpAppender: successfully sent event [{}] to {}", event.getLoggerName(), url);
        } catch (Exception e) {
            addError("Failed to send log", e);
        }
    }

    private void sendLog(String jsonLog) throws Exception {
        logger.debug("HttpAppender: opening HTTP connection to {}", url);
        URL endpoint = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + this.bearerToken);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] payload = jsonLog.getBytes(StandardCharsets.UTF_8);
            logger.debug("HttpAppender: writing {} bytes of payload", jsonLog.getBytes(StandardCharsets.UTF_8).length);
            os.write(payload, 0, payload.length);
        }

        int code = conn.getResponseCode();
        logger.debug("HttpAppender: received HTTP {}", code);
        if (code != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Log HTTP error: " + code);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
