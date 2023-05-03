package com.client.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Table(name = "article")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "article_content")
    private String content;

    @Column(name = "category")
    private String category;

    @Column(name = "date_of_submission")
    private Timestamp dateOfSubmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journalist_id_a")
    private User journalist;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endorsement> endorsements;

    public void addEndorsement(Endorsement endorsement) {
        if (endorsement == null)
            return;
        if (endorsements == null)
            endorsements = new ArrayList<>();
        endorsements.add(endorsement);
        endorsement.setArticle(this);
    }

    public void removeEndorsement(Endorsement endorsement) {
        if (endorsement == null || endorsements == null)
            return;
        endorsements.remove(endorsement);
        endorsement.setArticle(null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Article other = (Article) o;
        return Objects.equals(title, other.title)
                && Objects.equals(content, other.content);
    }
    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
