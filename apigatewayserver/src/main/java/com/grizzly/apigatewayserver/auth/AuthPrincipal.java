package com.grizzly.apigatewayserver.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AuthPrincipal implements UserDetails {
    private AuthSession authSession;

    public AuthPrincipal(AuthSession authSession) {
        this.authSession = authSession;
    }

    @Override
    public String getUsername() {
        return authSession.getId().toString();
    }

    @Override
    public String getPassword() {
        return authSession.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(authSession.getRole()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}