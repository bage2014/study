package com.bage.filter;

import java.io.UnsupportedEncodingException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class AuthorizationFilter {

	public static void main(String[] args) {

		try {

			String compactJws = "eyJhbGciOiJIUzI1NiIsImN0eSI6IkpXVCJ9.eyJhZ2UiOiAyMX0.xHq8ROBnmLitd9rdmtB-Tx7t6ZYc9jV85Ig8QwRa1q8";
			byte[] key = "password".getBytes("UTF-8");
		    Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);

		    //OK, we can trust this JWT
		    System.out.println(Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().get("age"));

		} catch (SignatureException e) {

		    //don't trust the JWT!
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
