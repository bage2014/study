package com.bage.study.springboot.restresponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;


//@Component
public class ResponseBodyWrapHandler implements HandlerMethodReturnValueHandler {

	private final HandlerMethodReturnValueHandler delegate;
	private static final Logger log = LoggerFactory.getLogger(ResponseControllerAdvice.class);

	public ResponseBodyWrapHandler(HandlerMethodReturnValueHandler delegate) {

		this.delegate = delegate;

	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {

		return delegate.supportsReturnType(returnType);

	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest) throws Exception {

		RestReresponse rtnMsg = null;
		if (returnValue instanceof RestReresponse) {
			log.error("beforeBodyWrite String data = {}",returnValue);
			rtnMsg = (RestReresponse) returnValue;
		} else {
			log.error("beforeBodyWrite other data = {}",returnValue);
			rtnMsg = new RestReresponse(200, returnValue);
		}

		delegate.handleReturnValue(rtnMsg, returnType, mavContainer, webRequest);

	}

}