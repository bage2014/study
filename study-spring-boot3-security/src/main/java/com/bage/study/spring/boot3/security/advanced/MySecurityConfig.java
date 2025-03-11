//package com.bage.study.spring.boot3.security.advanced;
//
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
//import org.springframework.security.authorization.AuthorityAuthorizationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.jwt.*;
//import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
//import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
//
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.time.Duration;
//import java.util.List;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class MySecurityConfig {
//
//    @Value("${jwt.public.key}")
//    RSAPublicKey key;
//
//    @Value("${jwt.private.key}")
//    RSAPrivateKey priv;
//
//    @Bean
//    @ConditionalOnMissingBean(UserDetailsService.class)
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user")
//                .roles("USER")
//                .build());
//
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build());
//        return manager;
//    }
//
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration registration = ClientRegistration
//                .withRegistrationId("messaging-client")
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .clientId("messaging-client")
//                .clientSecret("secret")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .scope("read","write")
//                .tokenUri("http://127.0.0.1:8080/authorized")
//                .clientName("messaging-client-name")
//                .build();
//        return new InMemoryClientRegistrationRepository(List.of(registration));
//    }
//
//
//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder()
//                .issuer("http://localhost:8080")
//                .build();
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        AuthorityAuthorizationManager<RequestAuthorizationContext> admin = AuthorityAuthorizationManager.hasRole("ADMIN");
//        admin.setRoleHierarchy(roleHierarchy());
//
//
//        AuthorityAuthorizationManager<RequestAuthorizationContext> user = AuthorityAuthorizationManager.hasRole("USER");
//        user.setRoleHierarchy(roleHierarchy());
//
//        http.authorizeHttpRequests(authorize -> authorize
//
//                        .requestMatchers("/api/admin/**")
//                        .access(admin)
//
//                        .requestMatchers("/api/user/**")
//                        .access(user)
//
//                        .requestMatchers("/api/anonymous/**")
//                        .anonymous()
//
//                        .anyRequest().authenticated()
//
//                )
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults())
//
//                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
//
//
//        ;
//        return http.build();
//    }
//
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
//        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF > ROLE_USER > ROLE_GUEST");
//        return hierarchy;
//    }
//
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        NimbusJwtDecoder build = NimbusJwtDecoder
//                .withPublicKey(this.key)
//                .build();
//        return build;
//    }
//
//
//    @Bean
//    public JwtEncoder jwtEncoder() {
//        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
//        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
//        return new NimbusJwtEncoder(jwks);
//    }
//
//}