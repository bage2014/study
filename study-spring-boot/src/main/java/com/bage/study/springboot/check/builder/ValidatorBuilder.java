package com.bage.study.springboot.check.builder;

import com.bage.study.springboot.check.ValidateResult;

public class ValidatorBuilder {

    private ValidateResult validateResult = new ValidateResult();

    public ValidatorBuilder success() {
        setOk(true);
        setMsg("validate success");
        return this;
    }

    public ValidatorBuilder failure() {
        setOk(false);
        setMsg("validate failure");
        return this;
    }

    public ValidatorBuilder setOk(boolean ok) {
        validateResult.setOk(ok);
        return this;
    }

    public ValidatorBuilder setMsg(String msg) {
        validateResult.setMsg(msg);
        return this;
    }

    public ValidateResult build() {
        return validateResult;
    }
}
