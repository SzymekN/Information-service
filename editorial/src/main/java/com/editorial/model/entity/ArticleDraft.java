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
@Table(name = "article_draft")
public class ArticleDraft implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_draft")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date_of_update")
    private Timestamp dateOfUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journalist_id_d")
    private User journalist;

    @Override
    public int hashCode() {
        return Objects.hash(title, content, dateOfUpdate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        ArticleDraft other = (ArticleDraft) o;
        return Objects.equals(title, other.title)
                && Objects.equals(content, other.content)
                && Objects.equals(dateOfUpdate, other.dateOfUpdate);
    }
}
