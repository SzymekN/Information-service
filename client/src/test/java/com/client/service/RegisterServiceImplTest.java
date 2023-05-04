package com.client.service;

import com.client.config.TestDbConfig;
import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import com.client.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@OverrideAutoConfiguration(enabled = true)
@ActiveProfiles("testdb")
@ContextConfiguration(classes = TestDbConfig.class)
public class RegisterServiceImplTest {

    private UserRegistrationDto userRegistrationDto;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegisterService registerService;

    @BeforeEach
    public void setUp() {
        userRegistrationDto = UserRegistrationDto.builder()
                .name("Ted")
                .surname("Stones")
                .email("tedStones@gmail.com")
                .supplier("APP")
                .username("tedstone")
                .password("password123")
                .authorityName("USER")
                .build();
    }

    @Test
    public void should_return_true_if_user_exists_with_particular_username() {
        // given
        User user = User.builder()
                .username(userRegistrationDto.getUsername())
                .password(userRegistrationDto.getPassword())
                .enabled(true)
                .build();
        userRepository.save(user);

        // when
        boolean result = registerService.checkIfUserExistsByUsername("tedstone");

        // then
        assertTrue(result);
    }

    @Test
    public void should_return_false_if_user_does_not_exist_with_particular_username() {
        // given & when
        boolean result = registerService.checkIfUserExistsByUsername("nonuser");

        // then
        assertFalse(result);
    }

    @Test
    public void should_convert_dto_to_user_details() {
        // given & when
        UserDetails userDetails = registerService.dtoToUserDetails(userRegistrationDto);

        // then
        assertNotNull(userDetails);
        assertEquals("Ted", userDetails.getName());
        assertEquals("Stones", userDetails.getSurname());
        assertEquals("tedStones@gmail.com", userDetails.getEmail());
        assertEquals("APP", userDetails.getSupplier());
    }

    @Test
    public void should_convert_dto_to_user() {
        // given & when
        User user = registerService.dtoToUser(userRegistrationDto);

        // then
        assertNotNull(user);
        assertEquals("tedstone", user.getUsername());
        assertEquals(true, user.getEnabled());
    }

    @Test
    public void should_register_user() {
        // given & when
        registerService.registerUser(userRegistrationDto);

        // then
        User savedUser = userRepository.findUserByName(userRegistrationDto.getUsername());
        assertNotNull(savedUser);
        assertEquals(userRegistrationDto.getUsername(), savedUser.getUsername());
        assertEquals(userRegistrationDto.getName(), savedUser.getUserDetails().getName());
        assertEquals(userRegistrationDto.getSurname(), savedUser.getUserDetails().getSurname());
        assertEquals(userRegistrationDto.getEmail(), savedUser.getUserDetails().getEmail());
        assertEquals(userRegistrationDto.getAuthorityName(), savedUser.getAuthority().getAuthorityName());
    }
}
