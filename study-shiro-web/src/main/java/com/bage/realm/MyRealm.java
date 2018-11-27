package com.bage.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.Realm;

public class MyRealm implements Realm{

	public String getName() {
		System.out.println("String com.bage.realm.MyRealm.getName() is work");
		return "myRealm";
	}

	/**
	 * 第一个会调用的函数
	 */
	public boolean supports(AuthenticationToken token) {
		System.out.println("boolean com.bage.realm.MyRealm.supports(AuthenticationToken token) is work");
		return token != null;
	}

	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("AuthenticationInfo com.bage.realm.MyRealm.getAuthenticationInfo(AuthenticationToken token) is work");
		
		Object principal = token.getPrincipal();
		Object credentials = token.getCredentials();
		return new SimpleAuthenticationInfo(principal , credentials, "myRealm");
	}

}
