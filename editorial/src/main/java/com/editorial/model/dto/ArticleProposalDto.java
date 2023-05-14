package com.editorial.model.dto;

import com.editorial.model.entity.ArticleProposal;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleProposalDto {

    private Long id;

    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @Size(min = 4, max = 200, message = "Keywords must contain more than 3 and less than 201 characters!")
    private String keywords;

    private ArticleProposal.Acceptance acceptance;

    private String authorName;

    @Override
    public int hashCode() {
        return Objects.hash(title, keywords);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleProposalDto that = (ArticleProposalDto) o;
        return title.equals(that.title) && keywords.equals(that.keywords);
    }
}
