package com.editorial.repository;

import com.editorial.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE u.username = :username")
    User findUserByName(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.userDetails ud " +
            "WHERE ud.email = :email")
    boolean existsUsersByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u " +
            "WHERE u.username = :username")
    boolean existsUsersByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id =:id")
    void deleteUserById(Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE u.id = :id")
    User findUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua")
    Slice<User> findAllPaged(Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ua.authorityName = :role")
    Slice<User> findAllByRolePaged(Pageable pageable, @Param("role") String role);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE u.username LIKE %:username%")
    Slice<User> findAllByUsernamePaged(Pageable pageable, @Param("username") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ud.name LIKE %:name%")
    Slice<User> findAllByNamePaged(Pageable pageable, @Param("name") String name);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ud.surname LIKE %:surname%")
    Slice<User> findAllBySurnamePaged(Pageable pageable, @Param("surname") String surname);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ud.email LIKE %:email%")
    Slice<User> findUserByEmail(Pageable pageable, @Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ua.authorityName = :role AND u.username LIKE %:username%")
    Slice<User> findAllByUsernameAndRolePaged(Pageable pageable, @Param("username") String username, @Param("role") String role);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ua.authorityName = :role AND ud.name LIKE %:name%")
    Slice<User> findAllByNameAndRolePaged(Pageable pageable, @Param("name") String name, @Param("role") String role);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ua.authorityName = :role AND ud.surname LIKE %:surname%")
    Slice<User> findAllBySurnameAndRolePaged(Pageable pageable, @Param("surname") String surname, @Param("role") String role);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails ud " +
            "JOIN FETCH u.authority ua WHERE ua.authorityName = :role AND ud.email LIKE %:email%")
    Slice<User> findUserByEmailAndRole(Pageable pageable, @Param("email") String email, @Param("role") String role);

    @Query("SELECT COUNT(u) FROM User u")
    Long countAllUsers();

    @Query("SELECT COUNT(u) FROM User u JOIN u.authority ua WHERE ua.authorityName = :role")
    Long countAllByRole(@Param("role") String role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.username LIKE %:username%")
    Long countAllByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) FROM User u JOIN u.userDetails ud WHERE ud.name LIKE %:name%")
    Long countAllByName(@Param("name") String name);

    @Query("SELECT COUNT(u) FROM User u JOIN u.userDetails ud WHERE ud.surname LIKE %:surname%")
    Long countAllBySurname(@Param("surname") String surname);

    @Query("SELECT COUNT(u) FROM User u JOIN u.userDetails ud " +
            "JOIN u.authority ua WHERE ua.authorityName = :role AND u.username LIKE %:username%")
    Long countAllByUsernameAndRole(@Param("username") String username, @Param("role") String role);

    @Query("SELECT COUNT(u) FROM User u JOIN u.userDetails ud " +
            "JOIN u.authority ua WHERE ua.authorityName = :role AND ud.name LIKE %:name%")
    Long countAllByNameAndRole(@Param("name") String name, @Param("role") String role);

    @Query("SELECT COUNT(u) FROM User u JOIN u.userDetails ud " +
            "JOIN u.authority ua WHERE ua.authorityName = :role AND ud.surname LIKE %:surname%")
    Long countAllBySurnameAndRole(@Param("surname") String surname, @Param("role") String role);
}