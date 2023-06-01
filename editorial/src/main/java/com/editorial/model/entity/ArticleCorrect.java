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
@Table(name = "article_correct")
public class ArticleCorrect implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correct")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date_of_correction")
    private Timestamp dateOfCorrection;

    @Column(name = "is_corrected")
    private Boolean isCorrected;

    @Column(name = "journalist_id_c")
    private Long journalistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corrector_id_c")
    private User corrector;

    @Override
    public int hashCode() {
        return Objects.hash(title, content, dateOfCorrection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        ArticleCorrect other = (ArticleCorrect) o;
        return Objects.equals(title, other.title)
                && Objects.equals(content, other.content)
                && Objects.equals(dateOfCorrection, other.dateOfCorrection);
    }
}
