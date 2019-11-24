package com.bage.study.springboot.check;

public interface ParamValidator<T> {

    /**
     * 参数校验
     *
     * @param param
     * @return
     */
    ValidateResult validate(T param);

    /**
     * 参数校验名称标志（唯一）
     *
     * @return
     */
    String getName();

}
