package com.bage.study.grayvalidator.core;

import com.bage.study.grayvalidator.core.config.GrayRule;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public class WhitelistMatcher {

    private final List<GrayRule>      rules;
    private final GrayFieldExtractor  extractor;
    private final List<FieldResolver> resolvers;
    private final String              globalGrayTargetUrl;

    public WhitelistMatcher(List<GrayRule> rules, GrayFieldExtractor extractor,
                            List<FieldResolver> resolvers, String globalGrayTargetUrl) {
        this.rules               = rules;
        this.extractor           = extractor;
        this.resolvers           = resolvers;
        this.globalGrayTargetUrl = globalGrayTargetUrl;
    }

    public Optional<String> match(HttpServletRequest request) {
        for (GrayRule rule : rules) {
            Optional<String> raw = extractor.extract(request, rule);
            if (raw.isEmpty()) continue;

            String value = applyResolvers(rule.getField(), raw.get(), request);
            if (matches(value, rule)) {
                String target = rule.getTargetUrl() != null ? rule.getTargetUrl() : globalGrayTargetUrl;
                return Optional.of(target);
            }
        }
        return Optional.empty();
    }

    private String applyResolvers(String field, String rawValue, HttpServletRequest request) {
        for (FieldResolver resolver : resolvers) {
            Optional<String> resolved = resolver.resolve(field, rawValue, request);
            if (resolved.isPresent()) return resolved.get();
        }
        return rawValue;
    }

    private boolean matches(String value, GrayRule rule) {
        return switch (rule.getMatchType()) {
            case VALUES -> rule.getValues().contains(value);
            case PREFIX -> value.startsWith(rule.getPrefix());
            case SUFFIX -> value.endsWith(rule.getSuffix());
        };
    }
}