package com.bage.study.spring.boot3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

	@Autowired
	JwtEncoder encoder;

	@PostMapping("/token")
	public String token(Authentication authentication) {
		Instant now = Instant.now();
		long expiry = 36000L;
		// @formatter:off
//		String scope = authentication.getAuthorities().stream()
//				.map(GrantedAuthority::getAuthority)
//				.collect(Collectors.joining(" "));
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName())
//				.claim("scope", scope)
				.build();
		// @formatter:on
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

}