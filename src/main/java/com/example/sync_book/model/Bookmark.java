package com.example.sync_book.model;

import com.example.sync_book.validation.NoHtml;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity {
    @NotBlank
    @Size(min = 5, max = 128)
    @NoHtml
    String userId;

    @NotNull
    @Min(10000)
    Integer bookId;

    @NotNull
    @Min(0)
    Float lastPosition;
}