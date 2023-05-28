package com.client.model.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String authorityName;
    private String supplier;

    @Override
    public int hashCode() {
        return Objects.hash(username, name, surname, email, authorityName, supplier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        UserDto other = (UserDto) o;
        return Objects.equals(username, other.username)
                && Objects.equals(name, other.name)
                && Objects.equals(surname, other.surname)
                && Objects.equals(email, other.email)
                && Objects.equals(authorityName, other.authorityName)
                && Objects.equals(supplier, other.supplier);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", authorityName='" + authorityName + '\'' +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
