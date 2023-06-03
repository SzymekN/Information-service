package com.editorial.service;

import com.editorial.model.dto.UserDto;
import com.editorial.model.dto.UserEditDto;
import com.editorial.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserActionService {
    Optional<User> getLoggedUser();
    ResponseEntity<String> deleteUserEditorialToClient(Long userId, HttpServletRequest request);
    void updateUser(User user, UserEditDto userEditDto, Long loggedUserId);
    ResponseEntity<String> updateUserEditorialToClient(Long userId, Long loggedUserId, UserEditDto userEditDto, HttpServletRequest servletRequest);
    void deleteUserById(Long id);
    User findUserById(Long id);
    ResponseEntity<List<UserDto>> findAllUsersPaged(Pageable pageable);
    ResponseEntity<List<UserDto>> findAllUsersByRolePaged(Pageable pageable, String role);
    ResponseEntity<List<UserDto>> findAllUsersByAttributeNamePaged(Pageable pageable, String attributeName, String attributeValue);
    ResponseEntity<List<UserDto>> findAllUsersByAttributeNameAndRolePaged(Pageable pageable, String role, String attributeName, String attributeValue);
    User findUserByUsername(String username);
    UserDto getUserInfo(User user);
}
