package com.bage.study.springboot.ann;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageRequestParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(PageRequestAnno.class)) {
            return false;
        }
        Class<?> type = parameter.getParameterType();
        return type.isAssignableFrom(PageRequestParam.class);

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageRequestAnno anno = parameter.getParameterAnnotation(PageRequestAnno.class);
        if (anno == null) {
            return null;
        }
        Integer current = getIntParamNotNullByWebRequest(anno.currentName(), webRequest);
        Integer size = getIntParamNotNullByWebRequest(anno.sizeName(), webRequest);

        PageRequestParam pageParam = new PageRequestParam();
        pageParam.setPageIndex(current);
        pageParam.setPageSize(size);
        return pageParam;
    }

    private Integer getIntParamNotNullByWebRequest(String name, NativeWebRequest webRequest) {
        String value = webRequest.getParameter(name);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }
}
