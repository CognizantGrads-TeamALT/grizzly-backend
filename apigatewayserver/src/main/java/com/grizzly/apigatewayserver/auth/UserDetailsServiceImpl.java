package com.grizzly.apigatewayserver.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private AuthRepository authRepository;

    public UserDetailsServiceImpl(AuthRepository applicationUserRepository) {
        this.authRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String tokenId) throws UsernameNotFoundException {
        AuthSession authSession = authRepository.findByTokenId(tokenId);

        if (authSession == null) {
            throw new UsernameNotFoundException(tokenId);
        }

        return new AuthPrincipal(authSession);
    }
}