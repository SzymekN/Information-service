package com.editorial.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserDetailsTest {

    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = UserDetails.builder().build();
    }

    @Test
    public void should_connect_user_with_userDetails() {
        // given
        User user = User.builder().build();

        // when
        userDetails.connectUser(user);

        // then
        assertEquals(user, userDetails.getUser());
    }

    @Test
    public void should_not_connect_null_user_with_userDetails() {
        // given
        User user = null;

        // when
        userDetails.connectUser(user);

        // then
        assertNull(userDetails.getUser());
    }
}

