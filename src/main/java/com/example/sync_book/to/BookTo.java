package com.example.sync_book.to;

import com.example.sync_book.model.FileType;
import com.example.sync_book.model.Genre;
import com.example.sync_book.validation.NoHtml;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BookTo extends NamedTo{

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String author;

    @NotBlank
    @Size(min = 20, max = 1024)
    @NoHtml
    String description;

    @NotNull
    @Min(1584)
    @Max(2024)
    Integer publicationYear;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String sourceUrl;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String imageUrl;

    @NotNull
    FileType fileType;

    @NotBlank
    @Size(min = 2, max = 16)
    @NoHtml
    String language;

    @Size(min = 2, max = 128)
    @NoHtml
    String narrator;

    @Size(min = 2, max = 128)
    @NoHtml
    String translator;

    @NotNull
    Genre genre;

    public BookTo(Integer id, String name, String author, String description, Integer publicationYear, String sourceUrl, String imageUrl, FileType fileType, String language, String narrator, String translator, Genre genre) {
        super(id, name);
        this.author = author;
        this.description = description;
        this.publicationYear = publicationYear;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.language = language;
        this.narrator = narrator;
        this.translator = translator;
        this.genre = genre;
    }
}