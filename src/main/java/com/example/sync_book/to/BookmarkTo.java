package com.example.sync_book.to;

import com.example.sync_book.validation.NoHtml;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkTo extends BaseTo {
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

    public BookmarkTo(Integer id, String userId, Integer bookId, Float lastPosition) {
        super(id);
        this.userId = userId;
        this.bookId = bookId;
        this.lastPosition = lastPosition;
    }
}