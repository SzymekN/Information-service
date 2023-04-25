package com.client.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Table(name = "user_detail")
public class UserDetails implements Serializable {

    @Id
    @Column(name = "user_id_d")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "supplier")
    @NotBlank
    private String supplier;


    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id_d")
    private User user;

    public void connectUser(User user) {
        if (user == null)
            return;
        this.user = user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, email, supplier);
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
                && Objects.equals(supplier, other.supplier);
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
