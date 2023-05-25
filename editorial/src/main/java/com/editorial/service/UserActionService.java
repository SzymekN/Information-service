package com.editorial.service;

import com.editorial.model.dto.UserDto;
import com.editorial.model.dto.UserRegistrationDto;
import com.editorial.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserActionService {
    Optional<User> getLoggedUser();
    ResponseEntity<String> deleteUserEditorialToClient(Long userId, HttpServletRequest request);
    void updateUser(User user, UserRegistrationDto userRegistrationDto);
    ResponseEntity<String> updateUserEditorialToClient(Long userId, UserRegistrationDto userRegistrationDto, HttpServletRequest servletRequest);
    void deleteUserById(Long id);
    User findUserById(Long id);
    ResponseEntity<List<UserDto>> findAllUsersPaged(Pageable pageable);
    ResponseEntity<List<UserDto>> findAllUsersByRolePaged(Pageable pageable, String role);
    ResponseEntity<List<UserDto>> findAllUsersByFieldPaged(Pageable pageable, String field, String value);
    ResponseEntity<List<UserDto>> findAllUsersByFieldAndRolePaged(Pageable pageable, String role, String field, String value);
}
