package com.client.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
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
public class ArticleDto {
    private Long id;

    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @NotBlank(message = "Content must not be blank!")
    private String content;

    @NotBlank(message = "Category must be picked!")
    private String category;

    @Past(message = "Invalid date!")
    private Timestamp dateOfSubmission;

    private Long positiveEndorsements;
    private Long negativeEndorsements;
    private String authorName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return title.equals(that.title) && content.equals(that.content) && category.equals(that.category) && positiveEndorsements.equals(that.positiveEndorsements) && negativeEndorsements.equals(that.negativeEndorsements) && authorName.equals(that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, category, positiveEndorsements, negativeEndorsements, authorName);
    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", dateOfSubmission=" + dateOfSubmission +
                ", positiveEndorsements=" + positiveEndorsements +
                ", negativeEndorsements=" + negativeEndorsements +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
