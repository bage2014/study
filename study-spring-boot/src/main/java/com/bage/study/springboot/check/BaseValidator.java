package com.bage.study.springboot.check;

import java.util.Collection;
import java.util.Objects;

public abstract class BaseValidator<T> implements ParamValidator<T> {

    protected boolean isNullOrEmpty(String content) {
        return Objects.isNull(content) || Objects.equals("", content);
    }

    protected boolean isNullOrEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

}
