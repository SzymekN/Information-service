package com.editorial.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCorrectDto {

    private Long id;

    @Size(min = 3, max = 200, message = "Title must contain more than 2 and less than 201 characters!")
    @NotBlank(message = "Title must not be blank!")
    @Pattern(regexp = "^[^<>*%:&/\\\\]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\s]+[0-9]*$", message = "Title must not contain such characters as:<>*%:&/\\")
    private String title;

    @NotBlank(message = "Content must not be blank!")
    private String content;

    private Timestamp dateOfCorrection;

    @NotNull
    private Boolean isCorrected;

    private String journalistName;

    private String correctorName;

    private String category;
}
