package com.example.sync_book.to;

import com.example.sync_book.model.FileType;
import com.example.sync_book.model.Genre;
import com.example.sync_book.validation.NoHtml;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class BookTo extends NamedTo{

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String author;

    @NotBlank
    @Size(min = 20, max = 1024)
    @NoHtml
    String description;

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
}