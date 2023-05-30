package com.client.service;

import com.client.model.dto.UserEditDto;
import com.client.model.dto.UserDto;
import com.client.model.entity.User;
import com.client.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.client.util.UrlConstants.EDITORIAL_DELETE_USER_URL;
import static com.client.util.UrlConstants.EDITORIAL_EDIT_USER_URL;

@Service
public class UserActionServiceImpl implements UserActionService {

    private final UserRepository userRepository;
    private final BasicServiceImpl basicService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserActionServiceImpl(UserRepository userRepository, BasicServiceImpl basicService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.basicService = basicService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(userRepository.findUserByName(authentication.getName()));
    }

    @Override
    public ResponseEntity<String> deleteUserClientToEditorial(Long userId, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = basicService.copyHeadersFromRequest(request);
        headers.set("X-Caller", "DELETE_FROM_CLIENT");
        URI endpointUri = UriComponentsBuilder.fromUriString(EDITORIAL_DELETE_USER_URL)
                .queryParam("id", userId).build().toUri();
        return restTemplate.exchange(endpointUri, HttpMethod.DELETE, new HttpEntity<>(null, headers), String.class);
    }

    @Override
    public void updateUser(User userToEdit, UserEditDto userEditDto, Long loggedUserId) {
        editUserByDto(userToEdit, userEditDto, loggedUserId);
        userRepository.save(userToEdit);
    }

    @Override
    public ResponseEntity<String> updateUserClientToEditorial(Long userId, Long loggedUserId, UserEditDto userEditDto, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = basicService.copyHeadersFromRequest(request);
        headers.set("X-Caller", "EDIT_FROM_CLIENT");
        URI endpointUri = UriComponentsBuilder.fromUriString(EDITORIAL_EDIT_USER_URL)
                .queryParam("id", userId).queryParam("loggedId", loggedUserId).build().toUri();
        System.out.println(endpointUri);
        System.out.println(userEditDto.toString());
        return restTemplate.exchange(endpointUri, HttpMethod.PUT, new HttpEntity<>(userEditDto, headers), String.class);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public ResponseEntity<List<UserDto>> findAllUsersPaged(Pageable pageable) {
        Slice<User> pagedUsers = userRepository.findAllPaged(pageable);

        if (!pagedUsers.hasContent())
            return ResponseEntity.noContent().build();

        Long totalCount = userRepository.countAllUsers();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", totalCount.toString());
        return ResponseEntity.ok().headers(headers).body(usersToDto(pagedUsers.getContent()));
    }

    @Override
    public ResponseEntity<List<UserDto>> findAllUsersByRolePaged(Pageable pageable, String role) {
        Slice<User> pagedUsers = userRepository.findAllByRolePaged(pageable, role);

        if (!pagedUsers.hasContent())
            return ResponseEntity.noContent().build();

        Long totalCount = userRepository.countAllByRole(role);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", totalCount.toString());
        return ResponseEntity.ok().headers(headers).body(usersToDto(pagedUsers.getContent()));
    }

    @Override
    public ResponseEntity<List<UserDto>> findAllUsersByAttributeNamePaged(Pageable pageable, String attributeName, String attributeValue) {
        Slice<User> pagedUsers = null;
        Long totalCount = null;
        if ("username".equals(attributeName)) {
            pagedUsers = userRepository.findAllByUsernamePaged(pageable, attributeValue);
            totalCount = userRepository.countAllByUsername(attributeValue);
        } else if ("name".equals(attributeName)) {
            pagedUsers = userRepository.findAllByNamePaged(pageable, attributeValue);
            totalCount = userRepository.countAllByName(attributeValue);
        } else if ("surname".equals(attributeName)) {
            pagedUsers = userRepository.findAllBySurnamePaged(pageable, attributeValue);
            totalCount = userRepository.countAllBySurname(attributeValue);
        } else if ("email".equals(attributeName)) {
            pagedUsers = userRepository.findAllByEmail(pageable, attributeValue);
            totalCount = userRepository.countAllByEmail(attributeValue);
        }

        HttpHeaders headers = new HttpHeaders();
        if (totalCount == null || totalCount == 0) {
            headers.set("X-Total-Count", "0");
            return ResponseEntity.noContent().headers(headers).build();
        }
        headers.set("X-Total-Count", totalCount.toString());
        return ResponseEntity.ok().headers(headers).body(usersToDto(pagedUsers.getContent()));
    }

    @Override
    public ResponseEntity<List<UserDto>> findAllUsersByAttributeNameAndRolePaged(Pageable pageable, String role, String attributeName, String attributeValue) {
        Slice<User> pagedUsers = null;
        Long totalCount = null;
        if ("username".equals(attributeName)) {
            pagedUsers = userRepository.findAllByUsernameAndRolePaged(pageable, attributeValue, role);
            totalCount = userRepository.countAllByUsernameAndRole(attributeValue, role);
        } else if ("name".equals(attributeName)) {
            pagedUsers = userRepository.findAllByNameAndRolePaged(pageable, attributeValue, role);
            totalCount = userRepository.countAllByNameAndRole(attributeValue, role);
        } else if ("surname".equals(attributeName)) {
            pagedUsers = userRepository.findAllBySurnameAndRolePaged(pageable, attributeValue, role);
            totalCount = userRepository.countAllBySurnameAndRole(attributeValue, role);
        } else if ("email".equals(attributeName)) {
            pagedUsers = userRepository.findAllByEmailAndRole(pageable, attributeValue, role);
            totalCount = userRepository.countAllByEmailAndRole(attributeValue, role);
        }

        HttpHeaders headers = new HttpHeaders();
        if (totalCount == null || totalCount == 0) {
            headers.set("X-Total-Count", "0");
            return ResponseEntity.noContent().headers(headers).build();
        }
        headers.set("X-Total-Count", totalCount.toString());
        return ResponseEntity.ok().headers(headers).body(usersToDto(pagedUsers.getContent()));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    private void editUserByDto(User userToEdit, UserEditDto userEditDto, Long loggedUserId) {
        userToEdit.setUsername(userEditDto.getUsername());
        userToEdit.getUserDetails().setName(userEditDto.getName());
        userToEdit.getUserDetails().setSurname(userEditDto.getSurname());
        if (userToEdit.getUserDetails().getSupplier().equals("APP")) {
            if (userEditDto.getPasswordToChange() != null)
                userToEdit.setPassword(passwordEncoder.encode(userEditDto.getPasswordToChange()));
            if (!loggedUserId.equals(userToEdit.getId()))
                userToEdit.getAuthority().setAuthorityName(userEditDto.getAuthorityName());
        }
    }

    public List<UserDto> usersToDto(List<User> users) {
        return users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getUserDetails().getName())
                        .surname(user.getUserDetails().getSurname())
                        .email(user.getUserDetails().getEmail())
                        .supplier(user.getUserDetails().getSupplier())
                        .authorityName(user.getAuthority().getAuthorityName())
                        .build())
                .collect(Collectors.toList());
    }
}
