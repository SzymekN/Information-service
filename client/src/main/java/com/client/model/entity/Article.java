package com.client.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @Column(name = "article_content")
    @NotBlank(message = "Content must not be blank!")
    private String content;

    @Column(name = "category")
    @NotBlank(message = "Category must be picked!")
    private String category;

    @Column(name = "date_of_submission")
    @Past(message = "Invalid date!")
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
        return Objects.hash(title, content, dateOfSubmission);
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
                && Objects.equals(content, other.content)
                && Objects.equals(dateOfSubmission, other.dateOfSubmission);
    }
}
