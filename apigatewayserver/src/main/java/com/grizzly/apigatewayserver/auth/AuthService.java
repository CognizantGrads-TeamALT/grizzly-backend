package com.grizzly.apigatewayserver.auth;

import com.grizzly.apigatewayserver.client.UserClient;
import com.grizzly.apigatewayserver.security.GoogleAuthenticator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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
    public Payload verifyToken(String tokenId) {
        return GoogleAuthenticator.verifyIdToken(tokenId);
    }

    private AuthSession getSession(String tokenId) {
        return authRepository.findByTokenId(tokenId);
    }

    public synchronized AuthSession getActiveSession(String tokenId) {
        AuthSession authSession = authRepository.findByTokenId(tokenId);

        // Session doesn't exist.
        if (authSession == null) {
            System.out.println("Session does not exist?");
            // Fetch token data and validate.
            Payload tokenData = verifyToken(tokenId);

            // invalid token.
            if (tokenData == null) {
                System.out.println("No payload?");
                return null;
            }
        }

        System.out.println("Returning.");

        // Return existing session.
        return authSession;
    }

    // the method we'll be calling in the filter.
    public synchronized AuthSession sessionStart(String tokenId) {
        AuthSession authSession = authRepository.findByTokenId(tokenId);

        // Session doesn't exist.
        if (authSession == null) {
            // Fetch token data and validate.
            Payload tokenData = verifyToken(tokenId);

            // invalid token.
            if (tokenData == null) {
                return null;
            }

            // create a new session.
            authSession = createSession(tokenId, tokenData);
        }

        // Return existing session.
        return authSession;
    }

    // delete session
    public Boolean deleteSession(String tokenId) {
        AuthSession authSession = authRepository.findByTokenId(tokenId);

        if (authSession != null) {
            authRepository.delete(authSession);
            return true;
        }

        return false;
    }

    public Boolean hasAccess(AuthSession authSession, String expectedRole) {
        return authSession.getRole().equals(expectedRole);
    }

    // aka sign in.
    private synchronized AuthSession createSession(String tokenId, Payload tokenData) {
        // theres already a session...
        //AuthSession authSession = getSession(tokenId);
        //if (authSession != null)
        //    return authSession;

        // search for the user.
        Object user = userClient.findByUserEmail(tokenData.getEmail());

        // user doesn't exist, lets create them.
        if (user == null) {
            user = signUp(tokenData);

            // not happening...
            if (user == null)
                return null;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap) user;

        System.out.println("DATA FROM USERMICRO: ");
        System.out.println(map.toString());

        AuthSession authSession =
                new AuthSession(
                        tokenId,
                        map.get("role"),
                        tokenData.getEmail()
                );

        // Clear any expired tokens that may exist.
        clearExpiredSessions(tokenData.getEmail());

        // save.
        authRepository.save(authSession);

        return authSession;
    }

    // creating a new user.
    private Object signUp(Payload tokenData) {
        String email = tokenData.getEmail();
        String name = (String) tokenData.get("name");

        return userClient.addNewUser(name, email);
    }

    // clear the old expired sessions.
    private void clearExpiredSessions(String email) {
        List<AuthSession> authSessionList = authRepository.findByEmail(email);

        if (authSessionList != null)
            for (AuthSession authSession : authSessionList) {
                String tokenId = authSession.getTokenId();

                if (verifyToken(tokenId) == null)
                    authRepository.deleteByTokenId(tokenId);
            }
    }
}
