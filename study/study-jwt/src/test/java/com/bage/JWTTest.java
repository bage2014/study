package com.bage;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTTest {

	public static void main(String[] args) throws IllegalArgumentException, UnsupportedEncodingException {
		
		// jjwt();
		
		String token= "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

		
		try {
		    Algorithm algorithm = Algorithm.HMAC256("your-256-bit-secret");
		    //Algorithm algorithm = Algorithm.HMAC256("your-256-bit-secret".getBytes("UTF-8"));
		    JWTVerifier verifier = JWT.require(algorithm)
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		    System.out.println(jwt);
		} catch (JWTVerificationException exception){
		    //Invalid signature/claims
			exception.printStackTrace();
		}
		
	}

	private static void jjwt() {
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
