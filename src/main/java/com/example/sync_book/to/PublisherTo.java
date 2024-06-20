package com.example.sync_book.to;

import com.example.sync_book.validation.NoHtml;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PublisherTo extends NamedTo{

    @NotNull
    @Min(1584)
    @Max(2024)
    Integer foundationYear;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    String city;

    public PublisherTo(Integer id, String name, Integer foundationYear, String city) {
        super(id, name);
        this.foundationYear = foundationYear;
        this.city = city;
    }

    @Override
    public String toString() {
        return "PublisherTo:" + id + '['  + name + ' ' + city + ' ' + foundationYear + ']';
    }
}