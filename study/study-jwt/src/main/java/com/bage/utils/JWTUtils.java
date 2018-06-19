package com.bage.utils;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTUtils {

	public static String compactJws(String sub,SignatureAlgorithm alg, Key key) {
		return Jwts.builder()
		  .setSubject(sub)
		  .signWith(alg, key)
		  .compact();
		
	}

	public static String compactJws(String sub,SignatureAlgorithm alg) {
		Key key = MacProvider.generateKey();
		return compactJws(sub,alg,key);
		
	}
	
	public static String compactJws(String sub) {
		Key key = MacProvider.generateKey();
		return Jwts.builder()
		  .setSubject(sub)
		  .signWith(SignatureAlgorithm.HS512, key)
		  .compact();
		
	}

	public static Jws<Claims> parse(String claimsJws,Key key) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
		
	}
	
	public static Jws<Claims> parse(String compactJws) {
		Key key = MacProvider.generateKey();
		return parse(compactJws,key);
		
	}
	

	
}
