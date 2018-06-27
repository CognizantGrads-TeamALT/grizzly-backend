package com.grizzly.apigatewayserver.auth;

import com.grizzly.apigatewayserver.client.UserDTO;
import com.grizzly.apigatewayserver.client.UserClient;
import com.grizzly.apigatewayserver.security.GoogleAuthenticator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthRepository authRepository;

    /**
     * This will verify if the token is not expired, and is authenticated for
     * our application.
     * @param tokenId
     * @return Payload data
     */
    private Payload verifyToken(String tokenId) {
        return GoogleAuthenticator.verifyIdToken(tokenId);
    }

    public AuthSession sessionStart(String tokenId) {
        AuthSession authSession = authRepository.findByTokenId(tokenId);

        // Session doesn't exist.
        if (authSession == null) {
            // Fetch token data and validate.
            Payload tokenData = verifyToken(tokenId);

            // invalid token.
            if (tokenData == null)
                return null;

            // create a new session.
            authSession = createSession(tokenId, tokenData);
        }

        // Return existing session.
        return authSession;
    }

    public Boolean hasAccess(AuthSession authSession, String expectedRole) {
        return authSession.getRole().equals(expectedRole);
    }

    // aka sign in.
    private AuthSession createSession(String tokenId, Payload tokenData) {
        // search for the user.
        Object user = userClient.findByUserEmail(tokenData.getEmail());

        // user doesn't exist, lets create them.
        if (user == null) {
            user = signUp(tokenData);

            // not happening...
            if (user == null) {
                return null;
            }
        }

        try {
            AuthSession authSession =
                    new AuthSession(
                            tokenId,
                            user.getClass().getField("role").toString(),
                            tokenData.getEmail()
                    );

            // Clear any expired tokens that may exist.
            clearExpiredSessions(tokenData.getEmail());

            return authSession;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    // creating a new user.
    private Object signUp(Payload tokenData) {
        String email = tokenData.getEmail();
        String name = (String) tokenData.get("name");
        UserDTO user = new UserDTO(name, email);

        return userClient.addNewUser(user);
    }

    // clear the old expired sessions.
    private void clearExpiredSessions(String email) {
        List<AuthSession> authSessionList = authRepository.findByEmail(email);

        if (authSessionList != null)
            for (AuthSession authSession : authSessionList) {
                String tokenId = authSession.getTokenId();

                if (verifyToken(tokenId) == null)
                    authRepository.deleteById(tokenId);
            }
    }
}
