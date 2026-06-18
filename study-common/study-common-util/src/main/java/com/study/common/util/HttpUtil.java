package com.study.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public final class HttpUtil {

    private static final int DEFAULT_TIMEOUT = 30000;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    private HttpUtil() {
    }

    public static String get(String url) {
        return get(url, null, DEFAULT_TIMEOUT);
    }

    public static String get(String url, Map<String, String> headers) {
        return get(url, headers, DEFAULT_TIMEOUT);
    }

    public static String get(String url, Map<String, String> headers, int timeout) {
        return execute(url, GET, headers, null, timeout);
    }

    public static String post(String url, String body) {
        return post(url, null, body, DEFAULT_TIMEOUT);
    }

    public static String post(String url, Map<String, String> headers, String body) {
        return post(url, headers, body, DEFAULT_TIMEOUT);
    }

    public static String post(String url, Map<String, String> headers, String body, int timeout) {
        return execute(url, POST, headers, body, timeout);
    }

    public static String put(String url, String body) {
        return put(url, null, body, DEFAULT_TIMEOUT);
    }

    public static String put(String url, Map<String, String> headers, String body) {
        return put(url, headers, body, DEFAULT_TIMEOUT);
    }

    public static String put(String url, Map<String, String> headers, String body, int timeout) {
        return execute(url, PUT, headers, body, timeout);
    }

    public static String delete(String url) {
        return delete(url, null, DEFAULT_TIMEOUT);
    }

    public static String delete(String url, Map<String, String> headers) {
        return delete(url, headers, DEFAULT_TIMEOUT);
    }

    public static String delete(String url, Map<String, String> headers, int timeout) {
        return execute(url, DELETE, headers, null, timeout);
    }

    private static String execute(String url, String method, Map<String, String> headers, String body, int timeout) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);

            if (headers != null) {
                headers.forEach(conn::setRequestProperty);
            }

            if (body != null && !body.isEmpty()) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                }
            }

            int responseCode = conn.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String response = sb.toString();
            log.debug("HTTP {} {} response: {}", method, url, response);
            return response;

        } catch (Exception e) {
            log.error("HTTP {} {} failed: {}", method, url, e.getMessage(), e);
            throw new RuntimeException("HTTP request failed", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.warn("Failed to close reader", e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}