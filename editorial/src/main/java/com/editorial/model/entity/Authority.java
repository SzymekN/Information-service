package com.editorial.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authority")
public class Authority {

    @Id
    @Column(name = "user_id_a")
    private Long id;

    @Column(name = "authority")
    private String authorityName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id_a")
    private User user;

    @Override
    public int hashCode() {
        return Objects.hash(id, authorityName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Authority other = (Authority) o;
        return Objects.equals(id, other.id)
                && Objects.equals(authorityName, other.authorityName);
    }
}
