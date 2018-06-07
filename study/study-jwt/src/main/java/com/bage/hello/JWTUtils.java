package com.bage.hello;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTUtils {

	public static void main(String[] args) {
		
		// We need a signing key, so we'll create one just for this example. Usually
		// the key would be read from your application configuration instead.
		Key key = MacProvider.generateKey();

		String compactJws = Jwts.builder()
		  .setSubject("Joe")
		  .signWith(SignatureAlgorithm.HS512, key)
		  .compact();
		System.out.println(compactJws);
		
		// assert Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe");
		
		try {

		    Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);

		    //OK, we can trust this JWT
		    System.out.println(jws.toString());

		} catch (SignatureException e) {

		    //don't trust the JWT!
		}
		
		
	}
	
}
