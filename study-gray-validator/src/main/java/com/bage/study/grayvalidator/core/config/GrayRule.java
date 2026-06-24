package com.bage.study.grayvalidator.core.config;

import java.util.Objects;
import java.util.Set;

public final class GrayRule {

    private final String      field;
    private final FieldSource source;
    private final MatchType   matchType;
    private final Set<String> values;
    private final String      prefix;
    private final String      suffix;
    private final String      targetUrl;

    private GrayRule(String field, FieldSource source, MatchType matchType,
                     Set<String> values, String prefix, String suffix, String targetUrl) {
        this.field     = Objects.requireNonNull(field, "field must not be null");
        this.source    = source;
        this.matchType = Objects.requireNonNull(matchType, "matchType must not be null");
        this.values    = values != null ? Set.copyOf(values) : Set.of();
        this.prefix    = prefix;
        this.suffix    = suffix;
        this.targetUrl = targetUrl;
    }

    public static GrayRuleBuilder byField(String field)   { return new GrayRuleBuilder(field, FieldSource.AUTO); }
    public static GrayRuleBuilder byHeader(String header) { return new GrayRuleBuilder(header, FieldSource.HEADER); }
    public static GrayRuleBuilder byQuery(String param)   { return new GrayRuleBuilder(param, FieldSource.QUERY_PARAM); }
    public static GrayRuleBuilder byBody(String jsonPath) { return new GrayRuleBuilder(jsonPath, FieldSource.BODY); }

    public String      getField()     { return field; }
    public FieldSource getSource()    { return source; }
    public MatchType   getMatchType() { return matchType; }
    public Set<String> getValues()    { return values; }
    public String      getPrefix()    { return prefix; }
    public String      getSuffix()    { return suffix; }
    public String      getTargetUrl() { return targetUrl; }

    public static final class GrayRuleBuilder {
        private final String      field;
        private final FieldSource source;
        private       String      targetUrl;

        GrayRuleBuilder(String field, FieldSource source) {
            this.field  = field;
            this.source = source;
        }

        public GrayRuleBuilder targetUrl(String url) {
            this.targetUrl = url;
            return this;
        }

        public GrayRule matchValues(String... vals) {
            return new GrayRule(field, source, MatchType.VALUES, Set.of(vals), null, null, targetUrl);
        }

        public GrayRule matchPrefix(String prefix) {
            return new GrayRule(field, source, MatchType.PREFIX, null, prefix, null, targetUrl);
        }

        public GrayRule matchSuffix(String suffix) {
            return new GrayRule(field, source, MatchType.SUFFIX, null, null, suffix, targetUrl);
        }
    }
}