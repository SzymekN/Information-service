package com.client.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.client.util.UrlConstants.GOOGLE_TOKEN_ENDPOINT;
import static com.client.util.UrlConstants.GOOGLE_USERINFO_ENDPOINT;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Override
    public String getTokenResponseBody(RestTemplate restTemplate, String code, String clientId, String redirectUri, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_ENDPOINT, request, String.class);
        return response.getBody();
    }

    @Override
    public String getAccessToken(String responseBody) throws JSONException {
        String accessToken = "";
        JSONObject jsonObject = new JSONObject(responseBody);
        accessToken = jsonObject.getString("access_token");
        return accessToken;
    }

    @Override
    public ResponseEntity<String> getUserInfoResponse(RestTemplate restTemplate, String accessToken) {
        HttpHeaders accessHeaders = new HttpHeaders();
        accessHeaders.setBearerAuth(accessToken);
        HttpEntity<String> accessEntity = new HttpEntity<>("", accessHeaders);
        return restTemplate.exchange(GOOGLE_USERINFO_ENDPOINT, HttpMethod.GET, accessEntity, String.class);
    }
}
