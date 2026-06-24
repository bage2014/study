package com.bage.study.grayvalidator.internal;

import com.bage.study.grayvalidator.demo.DemoResponse;
import com.bage.study.grayvalidator.infra.CachedBodyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/gray-echo")
public class GrayEchoController {

    private static final Logger log = LoggerFactory.getLogger(GrayEchoController.class);

    @GetMapping("/**")
    public DemoResponse echoGet(HttpServletRequest request) {
        logIncoming(request);
        return buildGrayResponse(request);
    }

    @PostMapping("/**")
    public DemoResponse echoPost(HttpServletRequest request) throws IOException {
        CachedBodyRequestWrapper wrapped = new CachedBodyRequestWrapper(request);
        logIncoming(wrapped);
        return buildGrayResponse(wrapped);
    }

    private void logIncoming(HttpServletRequest request) {
        if (!log.isInfoEnabled()) return;
        log.info(buildIncomingLog(request));
    }

    private String buildIncomingLog(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("[GrayEcho] received ");
        sb.append(request.getMethod()).append(" ").append(request.getRequestURI());

        String query = request.getQueryString();
        if (query != null) sb.append("?").append(query);

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            sb.append("\n  params   : ");
            params.forEach((k, vals) -> sb.append(k).append("=").append(String.join(",", vals)).append(" "));
        }

        List<String> headers = new ArrayList<>();
        Enumeration<String> names = request.getHeaderNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                headers.add(name + "=" + request.getHeader(name));
            }
        }
        if (!headers.isEmpty()) {
            sb.append("\n  headers  : ").append(String.join(", ", headers));
        }

        if (request instanceof CachedBodyRequestWrapper cached) {
            byte[] body = cached.getCachedBody();
            if (body != null && body.length > 0) {
                String bodyStr = new String(body, StandardCharsets.UTF_8);
                sb.append("\n  body     : ").append(bodyStr.length() > 512
                        ? bodyStr.substring(0, 512) + "...(truncated)"
                        : bodyStr);
            }
        }

        return sb.toString();
    }

    private DemoResponse buildGrayResponse(HttpServletRequest request) {
        String originalPath = request.getRequestURI().replaceFirst("/internal/gray-echo", "");
        String query = request.getQueryString();

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("uri", request.getRequestURI());
        if (query != null) meta.put("query", query);

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            Map<String, String> flatParams = new LinkedHashMap<>();
            params.forEach((k, v) -> flatParams.put(k, String.join(",", v)));
            meta.put("params", flatParams);
        }

        return DemoResponse.gray("gray-response:" + originalPath, meta);
    }
}