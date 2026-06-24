package com.bage.study.grayvalidator.core;

import com.bage.study.grayvalidator.core.config.GrayRule;
import com.bage.study.grayvalidator.infra.CachedBodyRequestWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class GrayFieldExtractor {

    private final ObjectMapper objectMapper;

    public GrayFieldExtractor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<String> extract(HttpServletRequest request, GrayRule rule) {
        return switch (rule.getSource()) {
            case QUERY_PARAM -> fromQueryParam(request, rule.getField());
            case HEADER      -> fromHeader(request, rule.getField());
            case PATH_VAR    -> fromPathVar(request, rule.getField());
            case BODY        -> fromBody(request, rule.getField());
            case AUTO        -> fromQueryParam(request, rule.getField())
                                    .or(() -> fromHeader(request, rule.getField()))
                                    .or(() -> fromPathVar(request, rule.getField()))
                                    .or(() -> fromBody(request, rule.getField()));
        };
    }

    private Optional<String> fromQueryParam(HttpServletRequest request, String field) {
        return Optional.ofNullable(request.getParameter(field));
    }

    private Optional<String> fromHeader(HttpServletRequest request, String field) {
        return Optional.ofNullable(request.getHeader(field));
    }

    @SuppressWarnings("unchecked")
    private Optional<String> fromPathVar(HttpServletRequest request, String field) {
        Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (!(attr instanceof Map<?, ?> vars)) return Optional.empty();
        return Optional.ofNullable((String) vars.get(field));
    }

    private Optional<String> fromBody(HttpServletRequest request, String field) {
        byte[] body = bodyBytes(request);
        if (body == null || body.length == 0) return Optional.empty();
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode node = resolveDotPath(root, field);
            if (node == null || node.isNull() || node.isMissingNode()) return Optional.empty();
            return Optional.of(node.asText());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private byte[] bodyBytes(HttpServletRequest request) {
        if (request instanceof CachedBodyRequestWrapper w) return w.getCachedBody();
        return null;
    }

    private JsonNode resolveDotPath(JsonNode root, String dotPath) {
        JsonNode current = root;
        for (String segment : dotPath.split("\\.")) {
            if (current == null || current.isMissingNode()) return null;
            current = current.get(segment);
        }
        return current;
    }
}