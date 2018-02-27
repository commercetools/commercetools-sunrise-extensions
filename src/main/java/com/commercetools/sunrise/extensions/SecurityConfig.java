package com.commercetools.sunrise.extensions;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${authenticationKey}")
    String expectedKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(new AuthenticationFilter(expectedKey), BasicAuthenticationFilter.class)
                .authorizeRequests().anyRequest().authenticated();
    }

    private static final class FunctionAuthenticationToken extends AbstractAuthenticationToken {
        private String key;

        public FunctionAuthenticationToken(final String key) {
            super(Collections.emptyList());
            this.key = key;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return key;
        }

        @Override
        public Object getPrincipal() {
            return "";
        }
    }

    private final class AuthenticationFilter extends OncePerRequestFilter {
        private String expectedKey;

        public AuthenticationFilter(String expectedKey) {
            this.expectedKey = expectedKey;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            final String key = request.getHeader("x-functions-key");
            if(expectedKey.equals(key)){
                Authentication auth = new FunctionAuthenticationToken(key);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        }

    }
}