package com.example.sync_book.util;

import com.example.sync_book.to.PublisherTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PublisherTestData {

    public static PublisherTo getNew() {
        return new PublisherTo(null, "name", 2005, "city name");
    }
}