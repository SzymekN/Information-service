package com.editorial.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @Column(name = "content")
    @NotBlank(message = "Content must not be blank!")
    private String content;

    @Column(name = "date_of_correction")
    private Timestamp dateOfCorrection;

    @Column(name = "is_corrected")
    @NotNull
    private Boolean isCorrected;

    @Column(name = "is_edited_by_journalist")
    @NotNull
    private Boolean isEditedByJournalist;

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
