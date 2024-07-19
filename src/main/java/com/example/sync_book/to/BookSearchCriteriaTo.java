package com.example.sync_book.to;

import com.google.common.base.Strings;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

@Schema(description = "Criteria for searching books")
public record BookSearchCriteriaTo(
        @Schema(description = "Author's name")
        String author,

        @Schema(description = "Book's name")
        String name,

        @Schema(description = "Book's genre")
        String genre,

        @Schema(description = "Publication year")
        Integer publicationYear


) {
    @Override
    public String toString() {
        return "author='" + (!Strings.isNullOrEmpty(author) ? author : "All") + '\'' +
                ", name='" + (!Strings.isNullOrEmpty(name) ? name : "All") + '\'' +
                ", genre='" + (!Strings.isNullOrEmpty(genre) ? genre : "All") + '\'' +
                ", publicationYear=" + (Optional.ofNullable(publicationYear).isPresent() ? publicationYear : "All");
    }
}