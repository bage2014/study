package com.bage.study.log.logback.mask;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BinaryOperator;

public class MaskRules {
  private static final BinaryOperator<String> NO_OP = (in, out) -> {
    throw new UnsupportedOperationException("Only needed for parallel streams!");
  };

  private final Set<MaskRule> rules = new LinkedHashSet<>();

  public void addRule(MaskRule.Definition definition) {
    rules.add(definition.rule());
  }

  public String apply(String input) {
    return rules.stream().reduce(input, (out, rule) -> rule.apply(out), NO_OP);
  }
}