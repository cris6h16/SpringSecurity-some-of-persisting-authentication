package org.cris6h16.springsecurity_persistingauthentication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ...
                .securityContext((securityContext) -> securityContext
                        // is a delegate that allows multiple `SecurityContextRepository` to be used
                        .securityContextRepository(new DelegatingSecurityContextRepository( // by ----- defaults in v6 ----- is impl
                                new RequestAttributeSecurityContextRepository(),//"persist" the serialized `SecurityContext` in request attribute
                                new HttpSessionSecurityContextRepository() // associates the `SecurityContext` to the `HttpSession`
                        ))
                );
        return http.build();
    }
}
