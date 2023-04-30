package com.client.service;

import com.client.model.entity.User;
import com.client.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.client.util.UrlConstants.EDITORIAL_DELETE_USER_URL;

@Service
public class UserActionServiceImpl implements UserActionService {

    private final UserRepository userRepository;
    private final BasicServiceImpl basicService;
    @Autowired
    public UserActionServiceImpl(UserRepository userRepository, BasicServiceImpl basicService) {
        this.userRepository = userRepository;
        this.basicService = basicService;
    }

    @Override
    public User getLoggedUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByName(authentication.getName());
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
}
