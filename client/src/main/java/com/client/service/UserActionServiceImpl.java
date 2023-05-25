package com.client.service;

import com.client.model.dto.UserDto;
import com.client.model.dto.UserRegistrationDto;
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
    public void updateUser(User user, UserRegistrationDto userRegistrationDto) {
        editUserByDto(user, userRegistrationDto);
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<String> updateUserClientToEditorial(Long userId, UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = basicService.copyHeadersFromRequest(request);
        headers.set("X-Caller", "EDIT_FROM_CLIENT");
        URI endpointUri = UriComponentsBuilder.fromUriString(EDITORIAL_EDIT_USER_URL)
                .queryParam("id", userId).build().toUri();
        return restTemplate.exchange(endpointUri, HttpMethod.PUT, new HttpEntity<>(userRegistrationDto, headers), String.class);
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
    public ResponseEntity<List<UserDto>> findAllUsersByFieldPaged(Pageable pageable, String field, String value) {
        Slice<User> pagedUsers = null;
        Long totalCount = null;
        if ("username".equals(field)) {
            pagedUsers = userRepository.findAllByUsernamePaged(pageable, value);
            totalCount = userRepository.countAllByUsername(value);
        } else if ("name".equals(field)) {
            pagedUsers = userRepository.findAllByNamePaged(pageable, value);
            totalCount = userRepository.countAllByName(value);
        } else if ("surname".equals(field)) {
            pagedUsers = userRepository.findAllBySurnamePaged(pageable, value);
            totalCount = userRepository.countAllBySurname(value);
        } else if ("email".equals(field)) {
            pagedUsers = userRepository.findUserByEmail(pageable, value);
            totalCount = 1L;
        }

        if (pagedUsers == null || !pagedUsers.hasContent() || totalCount == null)
            return ResponseEntity.noContent().build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", totalCount.toString());
        return ResponseEntity.ok().headers(headers).body(usersToDto(pagedUsers.getContent()));
    }

    @Override
    public ResponseEntity<List<UserDto>> findAllUsersByFieldAndRolePaged(Pageable pageable, String role, String field, String value) {
        Slice<User> pagedUsers = null;
        Long totalCount = null;

        if ("username".equals(field)) {
            pagedUsers = userRepository.findAllByUsernameAndRolePaged(pageable, value, role);
            totalCount = userRepository.countAllByUsernameAndRole(value, role);
        } else if ("name".equals(field)) {
            pagedUsers = userRepository.findAllByNameAndRolePaged(pageable, value, role);
            totalCount = userRepository.countAllByNameAndRole(value, role);
        } else if ("surname".equals(field)) {
            pagedUsers = userRepository.findAllBySurnameAndRolePaged(pageable, value, role);
            totalCount = userRepository.countAllBySurnameAndRole(value, role);
        } else if ("email".equals(field)) {
            pagedUsers = userRepository.findUserByEmailAndRole(pageable, value, role);
            totalCount = 1L;
        }
        if (totalCount == null || pagedUsers == null || !pagedUsers.hasContent())
            return ResponseEntity.noContent().build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", totalCount.toString());
        return ResponseEntity.ok().headers(headers).body(usersToDto(pagedUsers.getContent()));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    private void editUserByDto(User user, UserRegistrationDto userRegistrationDto) {
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.getUserDetails().setName(userRegistrationDto.getName());
        user.getUserDetails().setSurname(userRegistrationDto.getSurname());
        user.getUserDetails().setEmail(userRegistrationDto.getEmail());
    }

    public List<UserDto> usersToDto(List<User> users) {
        return users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getUserDetails().getName())
                        .surname(user.getUserDetails().getSurname())
                        .email(user.getUserDetails().getEmail())
                        .authorityName(user.getAuthority().getAuthorityName())
                        .build())
                .collect(Collectors.toList());
    }
}
