package com.example.sync_book.model;

import com.example.sync_book.validation.NoHtml;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@Table(name = "publishers")
public class Publisher extends NamedEntity{

    @NotNull
    @Min(1584)
    @Max(2024)
    Integer foundationYear;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String city;
}