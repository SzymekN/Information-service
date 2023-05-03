package com.client.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

import static com.client.util.AccountConstants.GOOGLE_SUPPLIER;
import static com.client.util.AccountConstants.ROLE_USER;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {

    @NotBlank(message = "Username must not be blank!")
    @Size(min = 3, max = 45, message = "Username must contain more than 2 and less than 46 characters!")
    private String username;

    @NotBlank(message = "Password must not be blank!")
    @Size(min = 4, max = 75, message = "Password must contain more than 3 and less than 76 characters!")
    @Pattern(regexp = "^[A-Za-z0-9#%@!&$./]+$", message = "Password must not contain whitespace characters! It can contain characters such as: A-Z, a-z, 0-9, #%@!&$/.")
    private String password;

    @NotBlank(message = "Name must not be blank!")
    @Size(min = 3, max = 45, message = "Name must contain more than 2 and less than 46 characters!")
    @Pattern(regexp = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+", message = "The user's name can only contain letters.")
    private String name;

    @NotBlank(message = "Surname must not be blank!")
    @Size(min = 2, max = 45, message = "Surname must contain more than 1 and less than 46 characters!")
    private String surname;

    @Email(regexp = ".+[@].+[\\.].+", message = "Invalid address!")
    @Size(max = 45, message = "Email must contain less than 46 characters!")
    @NotBlank(message = "Email must not be blank!")
    private String email;

    private String supplier;

    private String authorityName;

    public static UserRegistrationDto jsonToDto(JSONObject googleUserData) throws JSONException {
        return UserRegistrationDto.builder()
                .username(googleUserData.getString("email"))
                .password(UUID.randomUUID().toString().replaceAll("-", ""))
                .name(googleUserData.getString("given_name"))
                .surname(googleUserData.getString("family_name"))
                .email(googleUserData.getString("email"))
                .supplier(GOOGLE_SUPPLIER)
                .authorityName(ROLE_USER)
                .build();
    }


    @Override
    public int hashCode() {
        return Objects.hash(username, name, surname, email, supplier, authorityName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        UserRegistrationDto other = (UserRegistrationDto) o;
        return Objects.equals(username, other.username)
                && Objects.equals(name, other.name)
                && Objects.equals(surname, other.surname)
                && Objects.equals(email, other.email)
                && Objects.equals(supplier, other.supplier)
                && Objects.equals(authorityName, other.authorityName);
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", supplier='" + supplier + '\'' +
                ", authorityName='" + authorityName + '\'' +
                '}';
    }
}
