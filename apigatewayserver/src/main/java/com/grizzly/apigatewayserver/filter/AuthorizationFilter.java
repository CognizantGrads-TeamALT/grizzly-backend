package com.grizzly.apigatewayserver.filter;

import com.grizzly.apigatewayserver.auth.AuthService;
import com.grizzly.apigatewayserver.auth.AuthSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.grizzly.apigatewayserver.security.SecurityConstants.HEADER_STRING;
import static com.grizzly.apigatewayserver.security.SecurityConstants.TOKEN_PREFIX;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private AuthService authService;

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        if (authService == null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            authService = webApplicationContext.getBean(AuthService.class);
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            System.out.println(token.replace(TOKEN_PREFIX, ""));
            AuthSession authSession = authService.sessionStart(token.replace(TOKEN_PREFIX, ""));

            if (authSession != null) {
                System.out.println("Success login?");
                return new UsernamePasswordAuthenticationToken(authSession, null, new ArrayList<>());
            }
        }

        System.out.println("Fail login.");

        return null;
    }
}