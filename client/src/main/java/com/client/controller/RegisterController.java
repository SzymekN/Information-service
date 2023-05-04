package com.client.controller;

import com.client.model.dto.UserRegistrationDto;
import com.client.service.LoginService;
import com.client.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.client.util.AccountConstants.APP_SUPPLIER;
import static com.client.util.AccountConstants.ROLE_USER;
import static com.client.util.UrlConstants.EDITORIAL_REGISTRATION_URL;

@RestController
@RequestMapping("/client")
public class RegisterController {

    private final LoginService loginService;
    private final RegisterService registerService;

    @Autowired
    public RegisterController(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        if (loginService.checkIfUserExistsByEmail(userRegistrationDto.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided e-mail is already taken!");
        else if (registerService.checkIfUserExistsByUsername(userRegistrationDto.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided username is already taken!");

        RestTemplate restTemplate = new RestTemplate();
        userRegistrationDto.setSupplier(APP_SUPPLIER);
        userRegistrationDto.setAuthorityName(ROLE_USER);
        registerService.registerUser(userRegistrationDto);
        restTemplate.postForEntity(EDITORIAL_REGISTRATION_URL, userRegistrationDto, String.class);
        return ResponseEntity.ok("Correct registration process.");
    }

    @PostMapping("/registration/fe")
    public ResponseEntity<String> createUserAccountByEditorial(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        if (loginService.checkIfUserExistsByEmail(userRegistrationDto.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided e-mail is already taken!");
        else if (registerService.checkIfUserExistsByUsername(userRegistrationDto.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided username is already taken!");

        registerService.registerUser(userRegistrationDto);
        return ResponseEntity.ok("Correct registration process.");
    }
}
