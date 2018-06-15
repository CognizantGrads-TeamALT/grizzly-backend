package com.grizzly.usermicro.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    @Query(value = "SELECT u FROM user u WHERE u.userId = :userId", nativeQuery = true)
    List<User> findByUser(@Param("userId") String userId, Pageable pageable);
}