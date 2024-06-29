package com.example.sync_book.util;

import com.example.sync_book.to.PublisherTo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PublisherTestData {

    public static final MatcherFactory.Matcher<PublisherTo> PUBLISHER_TO_MATCHER = MatcherFactory.usingIterableAssertions(PublisherTo.class, "id");
    public static final int NOT_FOUND_ID = 1000;
    public static final String NOT_FOUND_MESSAGE = "Entity with id=" + NOT_FOUND_ID + " not found";
    public static final Integer PUBLISHER1_ID = 100001;
    public static final Integer PUBLISHER2_ID = 100002;
    public static final Integer PUBLISHER3_ID = 100003;
    public static final PublisherTo PUBLISHER1 = new PublisherTo(PUBLISHER1_ID, "Publisher 1", 2001, "City 1");
    public static final PublisherTo PUBLISHER2 = new PublisherTo(PUBLISHER2_ID, "Publisher 2", 2002, "City 2");
    public static final PublisherTo PUBLISHER3 = new PublisherTo(PUBLISHER3_ID, "Publisher 3", 2003, "City 3");
    public static final List<PublisherTo> PUBLISHER_TO_LIST = List.of(PUBLISHER1, PUBLISHER2, PUBLISHER3);

    public static PublisherTo getNew() {
        return new PublisherTo(null, "name", 2005, "city name");
    }
}