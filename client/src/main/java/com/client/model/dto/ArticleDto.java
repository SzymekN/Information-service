package com.client.model.dto;

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
    private String title;
    private String content;
    private String category;
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
