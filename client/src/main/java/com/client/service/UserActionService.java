package com.client.service;

import com.client.model.dto.UserDto;
import com.client.model.dto.UserEditDto;
import com.client.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserActionService {
    Optional<User> getLoggedUser();
    ResponseEntity<String> deleteUserClientToEditorial(Long userId, HttpServletRequest request);
    void updateUser(User user, UserEditDto userEditDto, Long loggedUserId);
    ResponseEntity<String> updateUserClientToEditorial(Long userId, Long loggedUserId, UserEditDto userEditDto, HttpServletRequest servletRequest);
    void deleteUserById(Long id);
    User findUserById(Long userId);
    ResponseEntity<List<UserDto>> findAllUsersPaged(Pageable pageable);
    ResponseEntity<List<UserDto>> findAllUsersByRolePaged(Pageable pageable, String role);
    ResponseEntity<List<UserDto>> findAllUsersByAttributeNamePaged(Pageable pageable, String attributeName, String attributeValue);
    ResponseEntity<List<UserDto>> findAllUsersByAttributeNameAndRolePaged(Pageable pageable, String role, String attributeName, String attributeValue);
    User findUserByUsername(String username);
    UserDto getUserInfo(User user);
}
