package com.example.sync_book.util;

import com.example.sync_book.to.BookmarkTo;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.example.sync_book.util.BookTestData.*;
import static com.example.sync_book.util.CommonTestData.USER1_ID;

@UtilityClass
public class BookmarkTestData {
    public static final MatcherFactory.Matcher<BookmarkTo> BOOKMARK_TO_MATCHER = MatcherFactory.usingIterableAssertions(BookmarkTo.class, "id");
    public static final Integer BOOKMARK1_ID = 1000101;
    public static final BookmarkTo BOOKMARK1 = new BookmarkTo(BOOKMARK1_ID, USER1_ID, BOOK1_ID, 11.11f);
    public static final BookmarkTo BOOKMARK2 = new BookmarkTo(1000102, "User_2_id", BOOK2_ID, 22.22f);
    public static final BookmarkTo BOOKMARK3 = new BookmarkTo(1000103, "User_3_id", BOOK3_ID, 44.44f);
    public static final BookmarkTo BOOKMARK4 = new BookmarkTo(1000104, "User_1_id", BOOK2_ID, 55.55f);
    public static final BookmarkTo BOOKMARK5 = new BookmarkTo(1000105, "User_1_id", BOOK3_ID, 66.66f);

    public static final List<BookmarkTo> BOOKMARK_LIST = List.of(BOOKMARK1, BOOKMARK4, BOOKMARK5);

    public static BookmarkTo getNew() {
        return new BookmarkTo(null, "New_user_id", BOOK1_ID, 77.77f);
    }

    public static BookmarkTo getUpdated() {
        return new BookmarkTo(BOOKMARK1_ID, "User_1_id", BOOK1_ID, 77.77f);
    }

    public static BookmarkTo getUnsafe() {
        return new BookmarkTo(null, "<script>alert(123)</script>", BOOK1_ID, 77.77f);
    }

    public static BookmarkTo getInvalid() {
        return new BookmarkTo(null,"", BOOK1_ID, 77.77f);
    }
}