package com.editorial.controller;

import com.editorial.model.dto.UserRegistrationDto;
import com.editorial.service.RegisterServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.editorial.util.AccountConstants.APP_SUPPLIER;
import static com.editorial.util.UrlConstants.CLIENT_REGISTRATION_URL;

@RestController
@RequestMapping("/editorial")
public class RegisterController {

    private final RegisterServiceImpl registerService;

    @Autowired
    public RegisterController(RegisterServiceImpl registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        if (registerService.checkIfUserExistsByEmail(userRegistrationDto.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided e-mail is already taken!");
        else if (registerService.checkIfUserExistsByUsername(userRegistrationDto.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided username is already taken!");

        RestTemplate restTemplate = new RestTemplate();
        userRegistrationDto.setSupplier(APP_SUPPLIER);
        registerService.registerUser(userRegistrationDto);
        restTemplate.postForEntity(CLIENT_REGISTRATION_URL, userRegistrationDto, String.class);
        return ResponseEntity.ok("Correct registration process.");
    }

    @PostMapping("/registration/fc")
    public ResponseEntity<String> createUserAccountByClient(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        if (registerService.checkIfUserExistsByEmail(userRegistrationDto.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided e-mail is already taken!");
        else if (registerService.checkIfUserExistsByUsername(userRegistrationDto.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided username is already taken!");

        registerService.registerUser(userRegistrationDto);
        return ResponseEntity.ok("Correct registration process.");
    }
}