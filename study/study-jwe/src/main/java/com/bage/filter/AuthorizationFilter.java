package com.bage.filter;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class AuthorizationFilter {

	public static void main(String[] args) {

		try {

			String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
			try {
			    DecodedJWT jwt = JWT.decode(token);
			    System.out.println(jwt.getSubject());
			} catch (JWTDecodeException exception){
			    //Invalid token
			}
			
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
