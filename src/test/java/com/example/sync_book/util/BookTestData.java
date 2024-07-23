package com.example.sync_book.util;

import com.example.sync_book.model.FileType;
import com.example.sync_book.model.Genre;
import com.example.sync_book.to.BookTo;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.example.sync_book.util.PublisherTestData.*;

@UtilityClass
public class BookTestData {
    public static final MatcherFactory.Matcher<BookTo> BOOK_TO_MATCHER = MatcherFactory.usingIterableAssertions(BookTo.class, "id");
    public static final Integer BOOK1_ID = 100004;
    public static final Integer BOOK2_ID = 100005;
    public static final Integer BOOK3_ID = 100006;
    public static final String AUTHOR_NAME = "Book 1 author";
    public static final Integer PUBLICATION_YEAR = 2021;
    public static final String BOOK_NAME = "Book 1";
    public static final BookTo BOOK1 = new BookTo(BOOK1_ID, BOOK_NAME, AUTHOR_NAME, "Book 1 description", PUBLICATION_YEAR, "http://book1source.com/book1.fb2", "http://book1image.com", FileType.TEXT, "ru", null, null, Genre.DETECTIVE, PUBLISHER1_ID);
    public static final BookTo BOOK2 = new BookTo(BOOK2_ID, "Book 2", "Book 2 author", "Book 2 description", 2022, "http://book2source.com/book2.mp2", "http://book2image.com", FileType.AUDIO, "en", null, null, Genre.HISTORY, PUBLISHER2_ID);
    public static final BookTo BOOK3 = new BookTo(BOOK3_ID, "Book 3", "Book 3 author", "Book 3 description", 2023, "http://book3source.com/book3.epub", "http://book3image.com", FileType.TEXT, "ru", null, null, Genre.FANTASY, PUBLISHER3_ID);
    public static final List<BookTo> BOOK_TO_LIST = List.of(BOOK1, BOOK2, BOOK3);

    public static BookTo getNew() {
        return BookTo.builder()
                .name("New book name")
                .author("New author")
                .description("New book description")
                .publicationYear(2024)
                .sourceUrl("http://source.com/NewBookName.fb2")
                .imageUrl("http://source.com/NewBookImageUrl.png")
                .fileType(FileType.TEXT)
                .language("en")
                .genre(Genre.GUIDE)
                .publisherId(PUBLISHER1_ID)
                .build();
    }
}