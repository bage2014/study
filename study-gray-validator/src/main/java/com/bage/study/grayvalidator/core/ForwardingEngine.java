package com.bage.study.grayvalidator.core;

import com.bage.study.grayvalidator.infra.CachedBodyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ForwardingEngine {

    private static final Logger log = LoggerFactory.getLogger(ForwardingEngine.class);

    private static final Set<String> HOP_BY_HOP = Set.of(
            "connection", "keep-alive", "proxy-authenticate", "proxy-authorization",
            "te", "trailers", "transfer-encoding", "upgrade"
    );

    private final HttpClient httpClient;

    public ForwardingEngine(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void forward(HttpServletRequest request, HttpServletResponse response, String targetUrl)
            throws IOException {
        ResponseEntity<byte[]> entity = forwardAsEntity(request, targetUrl);
        response.setStatus(entity.getStatusCode().value());
        entity.getHeaders().forEach((name, values) ->
                values.forEach(v -> response.addHeader(name, v)));
        byte[] body = entity.getBody();
        if (body != null && body.length > 0) {
            response.getOutputStream().write(body);
        }
        response.getOutputStream().flush();
    }

    public ResponseEntity<byte[]> forwardAsEntity(HttpServletRequest request, String targetUrl) {
        String fullUri = buildTargetUri(request, targetUrl);
        if (log.isInfoEnabled()) {
            log.info(buildGrayRoutingLog(request, fullUri));
        }
        try {
            byte[] reqBody = readBody(request);

            HttpRequest.BodyPublisher publisher = reqBody.length > 0
                    ? HttpRequest.BodyPublishers.ofByteArray(reqBody)
                    : HttpRequest.BodyPublishers.noBody();

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(fullUri))
                    .method(request.getMethod(), publisher);
            copyRequestHeaders(request, builder);

            HttpResponse<byte[]> resp = httpClient.send(builder.build(),
                    HttpResponse.BodyHandlers.ofByteArray());

            HttpHeaders headers = new HttpHeaders();
            resp.headers().map().forEach((name, vals) -> {
                if (!HOP_BY_HOP.contains(name.toLowerCase()) &&
                        !"content-length".equalsIgnoreCase(name)) {
                    vals.forEach(v -> headers.add(name, v));
                }
            });

            return ResponseEntity.status(resp.statusCode()).headers(headers).body(resp.body());

        } catch (HttpTimeoutException e) {
            log.error("Gray forward timeout, target={}", fullUri, e);
            return ResponseEntity.status(502).body("{\"error\":\"gateway timeout\"}".getBytes());
        } catch (IOException e) {
            log.error("Gray forward IO error, target={}", fullUri, e);
            return ResponseEntity.status(502).body("{\"error\":\"bad gateway\"}".getBytes());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(503).body("{\"error\":\"service unavailable\"}".getBytes());
        } catch (Exception e) {
            log.error("Gray forward unexpected error, target={}", fullUri, e);
            return ResponseEntity.status(500).body("{\"error\":\"internal server error\"}".getBytes());
        }
    }

    private String buildGrayRoutingLog(HttpServletRequest request, String fullTargetUri) {
        StringBuilder sb = new StringBuilder("[GrayRouting] ");

        sb.append(request.getMethod()).append(" ").append(request.getRequestURI());
        String query = request.getQueryString();
        if (query != null) sb.append("?").append(query);

        sb.append(" → ").append(fullTargetUri);

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            String paramStr = params.entrySet().stream()
                    .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                    .collect(Collectors.joining(", ", "{", "}"));
            sb.append(" | params=").append(paramStr);
        }

        List<String> headerEntries = new ArrayList<>();
        Enumeration<String> names = request.getHeaderNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (!HOP_BY_HOP.contains(name.toLowerCase())) {
                    headerEntries.add(name + "=" + request.getHeader(name));
                }
            }
        }
        if (!headerEntries.isEmpty()) {
            sb.append(" | headers={").append(String.join(", ", headerEntries)).append("}");
        }

        return sb.toString();
    }

    private String buildTargetUri(HttpServletRequest request, String targetBaseUrl) {
        String base  = targetBaseUrl.endsWith("/")
                ? targetBaseUrl.substring(0, targetBaseUrl.length() - 1) : targetBaseUrl;
        String path  = request.getRequestURI();
        String query = request.getQueryString();
        return query != null ? base + path + "?" + query : base + path;
    }

    private byte[] readBody(HttpServletRequest request) throws IOException {
        if (request instanceof CachedBodyRequestWrapper w)     return w.getCachedBody();
        if (request instanceof ContentCachingRequestWrapper w) return w.getContentAsByteArray();
        return request.getInputStream().readAllBytes();
    }

    private void copyRequestHeaders(HttpServletRequest request, HttpRequest.Builder builder) {
        Enumeration<String> names = request.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
            String name  = names.nextElement();
            String lower = name.toLowerCase();
            if (HOP_BY_HOP.contains(lower) || "host".equals(lower) || "content-length".equals(lower)) continue;
            builder.header(name, request.getHeader(name));
        }
        if (request.getContentType() != null) {
            builder.header("Content-Type", request.getContentType());
        }
    }
}