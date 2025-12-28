package com.servicepulse.gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class ServiceTokenAuthenticationFilter extends OncePerRequestFilter {

    private final String serviceToken;

    public ServiceTokenAuthenticationFilter(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = request.getHeader(SecurityConstants.SERVICE_TOKEN_HEADER);

        if (serviceToken.equals(token)) {
            var auth = new UsernamePasswordAuthenticationToken(
                    "service",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + SecurityConstants.SERVICE_ROLE))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
