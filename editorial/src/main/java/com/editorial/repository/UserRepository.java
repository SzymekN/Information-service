package com.editorial.repository;

import com.editorial.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua where u.username = :username")
    User findUserByName(@Param("username") String username);
}