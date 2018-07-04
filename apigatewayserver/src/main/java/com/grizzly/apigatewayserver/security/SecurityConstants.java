package com.grizzly.apigatewayserver.security;

public interface SecurityConstants {
    String HEADER_STRING = "Authorization";

    // Client and issuer should match the front-end.
    String AUTH_ISSUER = "accounts.google.com";
    String AUTH_CLIENT = "296954481305-plmc2jf1o7j7t0aignvp73arbk2mt3pq.apps.googleusercontent.com";
}