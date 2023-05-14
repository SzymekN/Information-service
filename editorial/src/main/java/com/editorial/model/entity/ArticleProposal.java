package com.editorial.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Table(name = "article_proposal")
public class ArticleProposal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proposal")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "acceptance")
    @Enumerated(EnumType.STRING)
    private Acceptance acceptance;

    @Column(name = "date_of_update")
    private Timestamp dateOfUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journalist_id_p")
    private User journalist;

    @Override
    public int hashCode() {
        return Objects.hash(title, keywords);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        ArticleProposal other = (ArticleProposal) o;
        return Objects.equals(title, other.title)
                && Objects.equals(keywords, other.keywords);
    }

    public enum Acceptance {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
