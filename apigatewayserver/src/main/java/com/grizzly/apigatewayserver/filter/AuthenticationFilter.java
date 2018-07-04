package com.grizzly.apigatewayserver.filter;

import com.grizzly.apigatewayserver.auth.AuthService;
import com.grizzly.apigatewayserver.auth.AuthSession;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.grizzly.apigatewayserver.security.SecurityConstants.HEADER_STRING;

@Component
public class AuthenticationFilter extends ZuulFilter {
    @Autowired
    private AuthService authService;

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String tokenId = request.getHeader(HEADER_STRING);
        if (tokenId != null) {
            AuthSession authSession = authService.getActiveSession(tokenId);

            if (authSession != null) {
                ctx.addZuulRequestHeader("User-Data", authSession.getUserData());
                System.out.println("Added data: " + authSession.getUserData());
            }
        }

        System.out.println("Hi there");
        return null;
    }
}