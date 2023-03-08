package com.editorial.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "user_detail")
public class UserDetails {

    @Id
    @Column(name = "user_id_d")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Name must not be blank!")
    @Size(min = 3, max = 45, message = "Name must contain more than 2 and less than 46 characters!")
    @Pattern(regexp = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+", message = "The user's name can only contain letters.")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "Surname must not be blank!")
    @Size(min = 2, max = 45, message = "Surname must contain more than 1 and less than 46 characters!")
    private String surname;

    @Column(name = "email")
    @Email(regexp = ".+[@].+[\\.].+", message = "Invalid address!")
    @Size(max = 45, message = "Email must contain less than 46 characters!")
    @NotBlank(message = "Email must not be blank!")
    private String email;

    @Column(name = "city")
    @NotBlank(message = "City must not be blank!")
    @Size(min = 2, max = 45, message = "City must contain more than 1 and less than 46 characters!")
    private String city;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id_d")
    private User user;

    public void connectUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, email, city);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        UserDetails other = (UserDetails) o;
        return Objects.equals(name, other.name)
                && Objects.equals(surname, other.surname)
                && Objects.equals(email, other.email)
                && Objects.equals(city, other.city);
    }
}
