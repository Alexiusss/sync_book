package com.example.sync_book.service;

import com.example.sync_book.model.Book;
import com.example.sync_book.repository.BookRepository;
import com.example.sync_book.to.BookTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.sync_book.util.ValidationUtil.assureIdConsistent;
import static com.example.sync_book.util.ValidationUtil.checkNew;

/**
 * Service class for managing books.
 * Provides methods to create, update, delete, and fetch books.
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookService {

    @Autowired
    BookRepository repository;

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return the BookTo DTO of the retrieved book
     */
    public BookTo get(int id) {
        log.info("get book {}", id);
        Book book = repository.getExisted(id);
        return convertToDto(book);
    }

    /**
     * Retrieves all books.
     *
     * @return a list of BookTo DTOs representing all books
     */
    public List<BookTo> getAll() {
        log.info("get all books");
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new book.
     *
     * @param book the BookTo DTO of the book to create
     * @return the BookTo DTO of the created book
     */
    @Transactional
    public BookTo create(BookTo book) {
        Assert.notNull(book, "book must not be null");
        checkNew(book);
        Book saved = repository.save(convertFromDto(book));
        log.info("create new book {}", saved);
        return convertToDto(saved);
    }

    /**
     * Updates an existing book.
     *
     * @param id     the ID of the book to update
     * @param bookTo the BookTo DTO with updated data
     */
    @Transactional
    public void update(int id, BookTo bookTo) {
        assureIdConsistent(bookTo, id);
        log.info("update book {}", id);
        Book book = repository.getExisted(id);
        book.setName(bookTo.getName());
        book.setAuthor(book.getAuthor());
        book.setDescription(book.getDescription());
        book.setPublicationYear(book.getPublicationYear());
        book.setSourceUrl(book.getSourceUrl());
        book.setImageUrl(book.getImageUrl());
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     */
    @Transactional
    public void delete(int id) {
        log.info("delete book {}", id);
        repository.deleteExisted(id);
    }

    /**
     * Converts a Book entity to a BookTo DTO.
     *
     * @param book the Book entity to convert
     * @return the corresponding BookTo DTO
     */
    private BookTo convertToDto(Book book) {
        return BookTo.builder()
                .id(book.id())
                .name(book.getName())
                .author(book.getAuthor())
                .description(book.getDescription())
                .publicationYear(book.getPublicationYear())
                .sourceUrl(book.getSourceUrl())
                .imageUrl(book.getImageUrl())
                .fileType(book.getFileType())
                .language(book.getLanguage())
                .narrator(book.getNarrator())
                .translator(book.getTranslator())
                .genre(book.getGenre())
                .build();
    }

    /**
     * Converts a BookTo DTO to a Book entity.
     *
     * @param bookTo the BookTo DTO to convert
     * @return the corresponding Book entity
     */
    private Book convertFromDto(BookTo bookTo) {
        return Book.builder()
                .name(bookTo.getName())
                .author(bookTo.getAuthor())
                .description(bookTo.getDescription())
                .publicationYear(bookTo.getPublicationYear())
                .fileName(getFileName(bookTo.getSourceUrl()).split("\\.")[0])
                .fileExtension(getFileName(bookTo.getSourceUrl()).split("\\.")[1])
                .sourceUrl(bookTo.getSourceUrl())
                .imageUrl(bookTo.getImageUrl())
                .fileType(bookTo.getFileType())
                .language(bookTo.getLanguage())
                .narrator(bookTo.getNarrator())
                .translator(bookTo.getTranslator())
                .genre(bookTo.getGenre())
                .build();
    }

    private String getFileName(String source) {
        String[] strings = source.split("/");
        return strings[strings.length - 1];
    }
}