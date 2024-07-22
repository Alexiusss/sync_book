package com.example.sync_book.util;

import com.example.sync_book.to.BookmarkTo;
import lombok.experimental.UtilityClass;

import static com.example.sync_book.util.BookTestData.BOOK1_ID;

@UtilityClass
public class BookmarkTestData {
    public static final MatcherFactory.Matcher<BookmarkTo> BOOKMARK_TO_MATCHER = MatcherFactory.usingIterableAssertions(BookmarkTo.class, "id");
    public static final int NOT_FOUND_ID = 1000;
    public static final String NOT_FOUND_MESSAGE = "Entity with id=" + NOT_FOUND_ID + " not found";

    public static BookmarkTo getNew() {
        return new BookmarkTo(null, "New_user_id", BOOK1_ID, 77.77f);
    }

    public static BookmarkTo getUnsafe() {
        return new BookmarkTo(null, "New_user_id", BOOK1_ID, 77.77f);
    }

    public static BookmarkTo getInvalid() {
        return new BookmarkTo(null,"", BOOK1_ID, 77.77f);
    }
}