package com.client.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "endorsement")
public class Endorsement implements Serializable {

    @EmbeddedId
    private EndorsementId id;

    @Column(name = "endorsement_value")
    @NotNull
    private Boolean value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_article", insertable = false, updatable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", insertable = false, updatable = false)
    private User user;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Endorsement other = (Endorsement) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Endorsement{" +
                "id=" + id +
                '}';
    }
}
