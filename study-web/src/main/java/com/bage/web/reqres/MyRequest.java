package com.bage.web.reqres;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MyRequest extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public MyRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        System.out.println("MyRequest.getParameter is work....");
        return "my-" + super.getParameter(name);
    }
}
