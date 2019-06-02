package com.bage.study.spring.boot.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class Role implements GrantedAuthority {

    private String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
