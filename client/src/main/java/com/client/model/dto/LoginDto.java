package com.client.model.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    private String username;
    private String password;

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        LoginDto other = (LoginDto) o;
        return Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "username='" + username + '\'' +
                '}';
    }
}
