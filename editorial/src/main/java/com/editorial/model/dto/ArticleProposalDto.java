package com.editorial.model.dto;

import com.editorial.model.entity.ArticleProposal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleProposalDto {

    private Long id;

    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @NotBlank(message = "Title must not be blank!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*[!?]?$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    private ArticleProposal.Acceptance acceptance;

    private String authorName;

    private Timestamp dateOfUpdate;

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleProposalDto that = (ArticleProposalDto) o;
        return title.equals(that.title);
    }
}
