package com.bage;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Jwts;

public class Jwt {

	public static void main(String[] args) {
		try {
			
			//方式1 jjwt
			String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
			System.out.println(Jwts.parser().setSigningKey("your-256-bit-secret".getBytes("utf-8"))
					.parseClaimsJws(token).getBody().getSubject());

			//方式2 auth0
			Algorithm algorithm = Algorithm.HMAC256("your-256-bit-secret");
			// Algorithm algorithm =
			// Algorithm.HMAC256("your-256-bit-secret".getBytes("UTF-8"));
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
			DecodedJWT jwt = verifier.verify(token);
			System.out.println(jwt);
			
		} catch (Exception exception) {
			// Invalid signature/claims
			exception.printStackTrace();
		}

	}
}
