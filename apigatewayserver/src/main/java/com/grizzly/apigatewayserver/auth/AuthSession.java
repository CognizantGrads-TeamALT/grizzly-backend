package com.grizzly.apigatewayserver.auth;

import javax.persistence.*;

@Entity(name="session")
@Table(name="session")
public class AuthSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")          private Integer id;

    @Lob
    @Column(name="tokenId")     private String tokenId;
    @Column(name="userData",    columnDefinition = "TEXT")
                                private String userData;
    @Column(name="email")       private String email;
    @Column(name="role")        private String role;

    public AuthSession() {
        super();
    }

    public AuthSession(String tokenId, String userData, String email, String role) {
        this.tokenId    = tokenId;
        this.userData   = userData;
        this.email      = email;
        this.role       = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AuthSession{" +
                "tokenId='" + tokenId + '\'' +
                ", userData='" + userData + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
