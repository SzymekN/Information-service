package com.editorial.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ArticleCorrectDto {

    private Long id;

    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @NotBlank(message = "Title must not be blank!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*[!?]?$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @NotBlank(message = "Content must not be blank!")
    private String content;

    private Timestamp dateOfCorrection;

    @NotNull
    private Boolean isCorrected;

    private String journalistName;

    private String correctorName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleCorrectDto that = (ArticleCorrectDto) o;
        return Objects.equals(title, that.title)
                && Objects.equals(content, that.content)
                && Objects.equals(journalistName, that.journalistName)
                && Objects.equals(correctorName, that.correctorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, content, journalistName, correctorName);
    }
}
