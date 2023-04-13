package com.client.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EndorsementId implements Serializable {

    @Column(name = "fk_article")
    private Long articleId;

    @Column(name = "fk_user")
    private Long userId;

    @Override
    public int hashCode() {
        return Objects.hash(articleId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        EndorsementId other = (EndorsementId) o;
        return Objects.equals(articleId, other.articleId)
                && Objects.equals(userId, other.userId);
    }

    @Override
    public String toString() {
        return "EndorsementId{" +
                "articleId=" + articleId +
                ", userId=" + userId +
                '}';
    }
}
