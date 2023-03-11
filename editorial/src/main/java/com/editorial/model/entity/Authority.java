package com.editorial.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
        return Objects.hash(authorityName);
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
        return Objects.equals(authorityName, other.authorityName);
    }
}
