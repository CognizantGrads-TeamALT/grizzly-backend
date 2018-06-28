package com.grizzly.apigatewayserver.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import static com.grizzly.apigatewayserver.security.SecurityConstants.AUTH_ISSUER;
import static com.grizzly.apigatewayserver.security.SecurityConstants.AUTH_CLIENT;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuthenticator {
    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();

    private static GoogleIdTokenVerifier verifier =
            new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setIssuer(AUTH_ISSUER)
                    .setAudience(Collections.singletonList(AUTH_CLIENT))
                    .build();

    public static Payload verifyIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                System.out.println(payload.toPrettyString());
                return payload;
            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (GeneralSecurityException e) {
            System.out.println("GeneralSecurityException");
        } catch (IOException e) {
            System.out.println("IOException");
        }

        return null;
    }
}
