package com.editorial.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditDto {
    @NotBlank(message = "Nazwa nie może być pusta!")
    @Size(min = 3, max = 45, message = "Nazwa musi zawierać minimum 3 i maksymalnie 45 znaków!")
    private String username;

    @Size(min = 4, max = 75, message = "Hasło musi zawierać więcej niż 3 i maksymalnie 45 znaków!")
    @Pattern(regexp = "^[A-Za-z0-9#%@!&$./]+$", message = "Hasło nie może zawierać białych znaków! Może zawierać znaki takie jak: A-Z, a-z, 0-9, #%@!&$/.")
    private String passwordToChange;

    private String passwordToConfirm;

    @NotBlank(message = "Imie nie może być puste!")
    @Size(min = 3, max = 45, message = "Imie musi zawierać minimum 3 i maksymalnie 45 znaków!")
    @Pattern(regexp = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+", message = "Imie użytkownika może zawierać jedynie litery.")
    private String name;

    @NotBlank(message = "Nazwisko nie może być puste!")
    @Size(min = 2, max = 45, message = "Nazwisko musi zawierać minimum 2 i maksymalnie 45 znaków!")
    private String surname;

    @Pattern(regexp = "^(USER|JOURNALIST|REDACTOR|CORRECTOR|ADMIN)$", message = "Niepoprawna rola!")
    @NotBlank(message = "Pole roli nie może być puste!")
    private String authorityName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEditDto that = (UserEditDto) o;
        return username.equals(that.username) && name.equals(that.name) && surname.equals(that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, surname);
    }

    @Override
    public String toString() {
        return "UserEditDto{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", authorityName='" + authorityName + '\'' +
                '}';
    }
}
