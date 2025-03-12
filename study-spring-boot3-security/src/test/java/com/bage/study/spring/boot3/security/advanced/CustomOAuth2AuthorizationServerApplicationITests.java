/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bage.study.spring.boot3.security.advanced;

import com.bage.study.spring.boot3.security.auth.server.OAuth2AuthorizationServerApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link OAuth2AuthorizationServerApplication}.
 *
 * @author Steve Riesenberg
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CustomOAuth2AuthorizationServerApplicationITests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetToken() throws Exception {
		// @formatter:off
		String token = this.getAccessToken("client1","secret1");
		System.out.println(token);
		// @formatter:on
	}


	@Test
	void testGetToken2() throws Exception {
		// @formatter:off
		String token = this.getAccessToken("client2","secret2");
		System.out.println(token);
		// @formatter:on
	}

	private String getAccessToken(String clientId,String secret) throws Exception {
		// @formatter:off
		MvcResult mvcResult = this.mockMvc.perform(post("/oauth2/token")
				.param("grant_type", "client_credentials")
				.param("scope", "read")
				.with(httpBasic(clientId, secret)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").exists())
				.andReturn();
		// @formatter:on

		String tokenResponseJson = mvcResult.getResponse().getContentAsString();
		Map<String, Object> tokenResponse = this.objectMapper.readValue(tokenResponseJson, new TypeReference<>() {
		});

		return tokenResponse.get("access_token").toString();
	}

}