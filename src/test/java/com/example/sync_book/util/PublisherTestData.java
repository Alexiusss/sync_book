package com.example.sync_book.util;

import com.example.sync_book.to.PublisherTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PublisherTestData {

    public static final int NOT_FOUND_ID = 1000;
    public static final String NOT_FOUND_MESSAGE = "Entity with id=" + NOT_FOUND_ID + " not found";

    public static PublisherTo getNew() {
        return new PublisherTo(null, "name", 2005, "city name");
    }
}