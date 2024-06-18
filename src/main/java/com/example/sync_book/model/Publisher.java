package com.example.sync_book.model;

import com.example.sync_book.validation.NoHtml;
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
public class Publisher extends NamedEntity{

    @NotBlank
    @Min(1584)
    @Max(2024)
    Integer foundationYear;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String city;
}