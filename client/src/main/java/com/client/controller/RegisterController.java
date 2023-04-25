package com.client.controller;

import com.client.model.dto.UserRegistrationDto;
import com.client.service.LoginServiceImpl;
import com.client.service.RegisterServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.client.util.Constants.EDITORIAL_REGISTRATION_URL;

@RestController
@RequestMapping("/client")
public class RegisterController {

    private final LoginServiceImpl loginService;
    private final RegisterServiceImpl registerService;

    @Autowired
    public RegisterController(LoginServiceImpl loginService, RegisterServiceImpl registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        if (loginService.checkIfUserExistsByEmail(userRegistrationDto.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided e-mail is already taken!");

        RestTemplate restTemplate = new RestTemplate();
        registerService.registerUser(userRegistrationDto);
        restTemplate.postForEntity(EDITORIAL_REGISTRATION_URL,userRegistrationDto, String.class);
        return ResponseEntity.ok("Correct registration process.");
    }

    @PostMapping("/registration/from-editorial")
    public ResponseEntity<String> createUserAccountByEditorial(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        if (loginService.checkIfUserExistsByEmail(userRegistrationDto.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided e-mail is already taken!");

        registerService.registerUser(userRegistrationDto);
        return ResponseEntity.ok("Correct registration process.");
    }
}
