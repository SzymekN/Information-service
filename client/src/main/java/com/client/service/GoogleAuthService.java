package com.client.service;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface GoogleAuthService {
    String getTokenResponseBody(RestTemplate restTemplate, String code, String clientId, String redirectUri, String clientSecret);
    String getAccessToken(String responseBody) throws JSONException;
    ResponseEntity<String> getUserInfoResponse(RestTemplate restTemplate, String accessToken);
}
