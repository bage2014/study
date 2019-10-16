package com.bage.study.spring.boot.oauth2.server.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer
public class MyAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    PasswordEncoder passwordEncoder;
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client").secret("secret").scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(20000)
                .refreshTokenValiditySeconds(20000);
    }

//	@Bean
//	public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
//		return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
//	}

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);//.userDetailsService(userDetailsService);
//		if (checkUserScopes)
//			endpoints.requestFactory(requestFactory());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new OAuth2Configuration.CustomTokenEnhancer();
        converter.setKeyPair(
                new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "password".toCharArray()).getKeyPair("jwt"));
        return converter;
    }

    /*
     * Add custom user principal information to the JWT token
     */
    class CustomTokenEnhancer extends JwtAccessTokenConverter {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//			User user = (User) authentication.getPrincipal();
//
//			Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
//
//			info.put("email", user.getEmail());
//
            DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
//			customAccessToken.setAdditionalInformation(info);

            return super.enhance(customAccessToken, authentication);
        }
    }

//    class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {
//        @Autowired
//        private TokenStore tokenStore;
//
//        public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
//            super(clientDetailsService);
//        }
//
//        @Override
//        public TokenRequest createTokenRequest(Map<String, String> requestParameters,
//                                               ClientDetails authenticatedClient) {
//            if (requestParameters.get("grant_type").equals("refresh_token")) {
//                OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(
//                        tokenStore.readRefreshToken(requestParameters.get("refresh_token")));
//                SecurityContextHolder.getContext()
//                        .setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getName(), null,
//                                userDetailsService.loadUserByUsername(authentication.getName()).getAuthorities()));
//            }
//            return super.createTokenRequest(requestParameters, authenticatedClient);
//        }
//    }


}