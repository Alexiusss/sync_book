package com.example.sync_book.to;

import com.example.sync_book.validation.NoHtml;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PublisherTo extends NamedTo{
    Integer foundationYear;
    @NoHtml
    String city;

    public PublisherTo(Integer id, String name, Integer foundationYear, String city) {
        super(id, name);
        this.foundationYear = foundationYear;
        this.city = city;
    }

    @Override
    public String toString() {
        return "PublisherTo:" + id + '[' + city + ' ' + foundationYear + ']';
    }
}