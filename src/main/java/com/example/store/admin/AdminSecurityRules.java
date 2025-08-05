package com.example.store.admin;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import com.example.store.common.SecurityRules;
import com.example.store.users.Role;

@Component
public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers("/admin/**").hasRole(Role.ADMIN.name());
    }
}
