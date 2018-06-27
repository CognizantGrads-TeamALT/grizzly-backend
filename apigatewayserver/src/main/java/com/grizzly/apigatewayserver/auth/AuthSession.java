package com.grizzly.apigatewayserver.auth;

import javax.persistence.*;

@Entity(name="session")
public class AuthSession {
    @Id
    @Column(name="tokenId")     private String tokenId;
    @Column(name="role")        private String role;
    @Column(name="email")       private String email;

    public AuthSession(String tokenId, String role, String email) {
        this.tokenId = tokenId;
        this.role = role;
        this.email = email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
