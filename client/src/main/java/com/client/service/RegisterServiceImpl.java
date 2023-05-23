package com.client.service;

import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.Authority;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import com.client.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

import static com.client.util.UrlConstants.EDITORIAL_REGISTRATION_URL;
import static com.client.util.UrlConstants.EDITORIAL_USER_ENABLE;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BasicServiceImpl basicService;
    @Value("${verificationEndpoint}")
    private String verificationEndpoint;
    @Value("${spring.mail.username}")
    private String sendingEmail;
    private final JavaMailSender javaMailSender;

    @Autowired
    public RegisterServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository,
                               BasicServiceImpl basicService, JavaMailSender javaMailSender) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.basicService = basicService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean checkIfUserExistsByUsername(String username) {
        return userRepository.existsUsersByUsername(username);
    }

    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) throws MessagingException, UnsupportedEncodingException {
        User user = dtoToUser(userRegistrationDto);
        UserDetails userDetails = dtoToUserDetails(userRegistrationDto);
        Authority authority = new Authority(userRegistrationDto.getAuthorityName());
        user.connectUserDetails(userDetails);
        userDetails.connectUser(user);
        user.connectAuthority(authority);
        authority.setUser(user);
        if (user.getUserDetails().getSupplier().equals("APP") &&
                user.getAuthority().getAuthorityName().equals("USER")) {
            user.setEnabled(false);
            sendVerificationEmail(user);
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails dtoToUserDetails(UserRegistrationDto userRegistrationDto) {
        return UserDetails.builder()
                .name(userRegistrationDto.getName())
                .surname(userRegistrationDto.getSurname())
                .email(userRegistrationDto.getEmail())
                .supplier(userRegistrationDto.getSupplier())
                .build();
    }

    @Override
    public User dtoToUser(UserRegistrationDto userRegistrationDto) {
        return User.builder()
                .username(userRegistrationDto.getUsername())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .enabled(true)
                .build();
    }

    @Override
    public ResponseEntity<String> registerUserClientToEditorial(UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = basicService.copyHeadersFromRequest(request);
        headers.set("X-Caller", "REGISTRATION_FROM_CLIENT");
        return restTemplate.exchange(EDITORIAL_REGISTRATION_URL, HttpMethod.POST, new HttpEntity<>(userRegistrationDto, headers), String.class);
    }

    @Override
    public ResponseEntity<String> validateCode(String code, HttpServletRequest request) {
        User notActivatedUser = userRepository.findUserByVCode(code);
        if (notActivatedUser != null) {
            notActivatedUser.setEnabled(true);
            notActivatedUser.setValidationCode(null);
            userRepository.save(notActivatedUser);
            return enableUserInEditorial(notActivatedUser.getId(), request);
        }
        return ResponseEntity.badRequest().body("Invalid code!");
    }

    public ResponseEntity<String> enableUserInEditorial(Long userId, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = basicService.copyHeadersFromRequest(request);
        headers.set("X-Caller", "ACCOUNT_ENABLE");
        return restTemplate.exchange(EDITORIAL_USER_ENABLE, HttpMethod.POST, new HttpEntity<>(userId, headers), String.class);
    }

    public void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        final int VALID_CODE_LENGTH = 5;
        String receiver = user.getUserDetails().getEmail();
        String validationCode = RandomStringUtils.randomAlphanumeric(VALID_CODE_LENGTH);
        String senderName = "Information Service";
        String title = "Please verify your registration";
        StrBuilder contentOfEmailMessage = initEmailMessageContent(user, validationCode);

        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMailHelper = new MimeMessageHelper(mimeMailMessage);
        mimeMailHelper.setFrom(sendingEmail, senderName);
        mimeMailHelper.setTo(receiver);
        mimeMailHelper.setSubject(title);
        mimeMailHelper.setText(contentOfEmailMessage.toString(), true);
        javaMailSender.send(mimeMailMessage);

        user.setValidationCode(validationCode);
    }

    public StrBuilder initEmailMessageContent(User user, String validationCode) {
        StrBuilder contentOfEmailMessage = new StrBuilder("Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Information Service.");
        contentOfEmailMessage.replaceAll("[[name]]", user.getUsername());
        contentOfEmailMessage.replaceAll("[[URL]]", verificationEndpoint + "?code=" + validationCode);
        return contentOfEmailMessage;
    }
}
