package com.example.sync_book.model;

import com.example.sync_book.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Book extends NamedEntity{

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String author;

    @NotBlank
    @Size(min = 20, max = 1024)
    @NoHtml
    String description;

    @NotBlank
    @Min(1584)
    @Max(2024)
    Integer publicationYear;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String fileName;

    @NotBlank
    @Size(min = 2, max = 8)
    @NoHtml
    String fileExtension;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String sourceUrl;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String imageUrl;

    @NotBlank
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

    @NotBlank
    Genre genre;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "publisher_id", nullable = false)
    Publisher publisher;
}