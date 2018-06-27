package com.grizzly.apigatewayserver.auth;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends PagingAndSortingRepository<AuthSession, String>{
    @Query("SELECT s FROM session s WHERE s.tokenId = :tokenId")
    AuthSession findByTokenId(@Param("tokenId") String tokenId);

    @Query("SELECT s FROM session s WHERE s.email = :email")
    List<AuthSession> findByEmail(@Param("email") String email);
}
