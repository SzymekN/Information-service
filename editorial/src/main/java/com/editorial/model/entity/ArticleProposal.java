package com.editorial.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Table(name = "article_proposal")
public class ArticleProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proposal")
    private Long id;

    @Column(name = "title")
    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @Column(name = "keywords")
    @Size(min = 4, max = 200, message = "Keywords must contain more than 3 and less than 201 characters!")
    private String keywords;

    @Column(name = "is_accepted")
    @NotNull
    private Boolean isAccepted;

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
}
