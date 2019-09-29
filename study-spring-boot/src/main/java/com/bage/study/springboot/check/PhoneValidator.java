package com.bage.study.springboot.check;

import com.bage.study.springboot.check.builder.ValidatorBuilder;
import org.springframework.stereotype.Component;

/**
 * 手机号校验
 */
@Component
public class PhoneValidator extends BaseValidator<String> {

    @Override
    public ValidateResult validate(String param) {
        ValidatorBuilder builder = new ValidatorBuilder();

        if (isNullOrEmpty(param)) {
            return builder.failure().setMsg("phone can not be null or empty !").build();
        }

        if (param.length() < 8 || param.length() > 11) {
            return builder.failure().setMsg("phone'length is invalidate !").build();
        }

        // 校验成功
        return builder.success().build();
    }

    @Override
    public String getName() {
        return ValidatorName.phone;
    }
}
