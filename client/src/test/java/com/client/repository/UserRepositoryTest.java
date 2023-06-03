package com.client.repository;

import com.client.config.TestDbConfig;
import com.client.model.entity.Authority;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@OverrideAutoConfiguration(enabled = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("testdb")
@ContextConfiguration(classes = TestDbConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    @BeforeAll
    public void setUp() {
        Authority auth = Authority.builder()
                .id(Long.parseLong("1"))
                .authorityName("USER")
                .build();

        UserDetails userDetails = UserDetails.builder()
                .id(Long.parseLong("1"))
                .name("Test")
                .surname("TestSurname")
                .email("Test1@email.com")
                .supplier("APP")
                .build();

        user = User.builder()
                .id(Long.parseLong("1"))
                .password("user")
                .username("user")
                .enabled(true)
                .authority(auth)
                .userDetails(userDetails)
                .endorsements(new ArrayList<>())
                        .build();
        auth.setUser(user);
        userDetails.setUser(user);
    }
    @Test
    public void find_user_by_name() {
        // given
        user.setUsername("john.doe");
        userRepository.save(user);
        // when
        User foundUser = userRepository.findUserByName("john.doe");
        // then
        assertNotNull(foundUser);
        assertEquals("john.doe", foundUser.getUsername());
    }

    @Test
    public void user_exists_by_email() {
        // given
        user.getUserDetails().setEmail("john.doe@example.com");
        userRepository.save(user);
        // when
        boolean exists = userRepository.existsUsersByEmail("john.doe@example.com");
        // then
        assertTrue(exists);
    }

    @Test
    public void user_exists_by_username() {
        // given
        user.setUsername("john");
        userRepository.save(user);
        // when
        boolean exists = userRepository.existsUsersByUsername("john");
        // then
        assertTrue(exists);
    }

    @Test
    public void delete_user_by_id() {
        // given
        userRepository.save(user);
        // when & then
        assertDoesNotThrow(() -> userRepository.deleteUserById(user.getId()));
    }

    @Test
    public void find_user_by_id() {
        // given
        userRepository.save(user);
        // when
        User foundUser = userRepository.findUserById(user.getId());
        // then
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void testFindAllPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Slice<User> userSlice = userRepository.findAllPaged(pageable);

        assertNotNull(userSlice);
        assertEquals(5, userSlice.getContent().size());
    }

    @Test
    public void testFindAllByRolePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String role = "ADMIN";
        Slice<User> userSlice = userRepository.findAllByRolePaged(pageable, role);

        assertNotNull(userSlice);
        assertEquals(1, userSlice.getContent().size());
    }

    @Test
    public void testFindAllByUsernamePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String username = "user";
        Slice<User> userSlice = userRepository.findAllByUsernamePaged(pageable, username);

        assertNotNull(userSlice);
        assertEquals(5, userSlice.getContent().size());
    }

    @Test
    public void testFindAllByNamePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String name = "UserName";
        Slice<User> userSlice = userRepository.findAllByNamePaged(pageable, name);

        assertNotNull(userSlice);
        assertEquals(4, userSlice.getContent().size());
    }

    @Test
    public void testFindAllBySurnamePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String surname = "UserSurname";
        Slice<User> userSlice = userRepository.findAllBySurnamePaged(pageable, surname);

        assertNotNull(userSlice);
        assertEquals(4, userSlice.getContent().size());
    }

    @Test
    public void testFindUserByEmail() {
        Pageable pageable = PageRequest.of(0, 10);
        String email = "UserEmail@email.com";
        Slice<User> userSlice = userRepository.findAllByEmail(pageable, email);

        assertNotNull(userSlice);
        assertEquals(2, userSlice.getContent().size());
    }

    @Test
    public void testFindAllByUsernameAndRolePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String username = "user";
        String role = "USER";
        Slice<User> userSlice = userRepository.findAllByUsernameAndRolePaged(pageable, username, role);

        assertNotNull(userSlice);
        assertEquals(2, userSlice.getContent().size());
    }

    @Test
    public void testFindAllByNameAndRolePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String name = "UserName";
        String role = "USER";
        Slice<User> userSlice = userRepository.findAllByNameAndRolePaged(pageable, name, role);

        assertNotNull(userSlice);
        assertEquals(1, userSlice.getContent().size());
    }

    @Test
    public void testFindAllBySurnameAndRolePaged() {
        Pageable pageable = PageRequest.of(0, 10);
        String surname = "UserSurname";
        String role = "USER";
        Slice<User> userSlice = userRepository.findAllBySurnameAndRolePaged(pageable, surname, role);

        assertNotNull(userSlice);
        assertEquals(1, userSlice.getContent().size());
    }

    @Test
    public void testFindUserByEmailAndRole() {
        Pageable pageable = PageRequest.of(0, 10);
        String email = "UserEmail@email.com";
        String role = "USER";
        Slice<User> userSlice = userRepository.findAllByEmailAndRole(pageable, email, role);

        assertNotNull(userSlice);
        assertEquals(1, userSlice.getContent().size());
    }

    @Test
    public void testCountAllUsers() {
        Long count = userRepository.countAllUsers();

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void testCountAllByRole() {
        String role = "ADMIN";
        Long count = userRepository.countAllByRole(role);

        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    public void testCountAllByUsername() {
        String username = "user";
        Long count = userRepository.countAllByUsername(username);

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void testCountAllByName() {
        String name = "UserName";
        Long count = userRepository.countAllByName(name);

        assertNotNull(count);
        assertEquals(4, count);
    }

    @Test
    public void testCountAllBySurname() {
        String surname = "UserSurname";
        Long count = userRepository.countAllBySurname(surname);

        assertNotNull(count);
        assertEquals(4, count);
    }

    @Test
    public void testCountAllByEmail() {
        String email = "UserEmail@email.com";
        Long count = userRepository.countAllByEmail(email);

        assertNotNull(count);
        assertEquals(2, count);
    }

    @Test
    public void testCountAllByUsernameAndRole() {
        String username = "UserSurname";
        String role = "USER";
        Long count = userRepository.countAllByUsernameAndRole(username, role);

        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    public void testCountAllByNameAndRole() {
        String name = "UserName";
        String role = "USER";
        Long count = userRepository.countAllByNameAndRole(name, role);

        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    public void testCountAllBySurnameAndRole() {
        String surname = "UserSurname";
        String role = "USER";
        Long count = userRepository.countAllBySurnameAndRole(surname, role);

        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    public void testCountAllByEmailAndRole() {
        String email = "UserEmail@email.com";
        String role = "USER";
        Long count = userRepository.countAllByEmailAndRole(email, role);

        assertNotNull(count);
        assertEquals(1, count);
    }
}
