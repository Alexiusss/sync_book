package com.example.sync_book.service;

import com.example.sync_book.model.Book;
import com.example.sync_book.model.Publisher;
import com.example.sync_book.repository.BookRepository;
import com.example.sync_book.repository.PublisherRepository;
import com.example.sync_book.to.BookSearchCriteriaTo;
import com.example.sync_book.to.BookTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

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
    BookRepository bookRepository;

    @Autowired
    PublisherRepository publisherRepository;

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return the BookTo DTO of the retrieved book
     */
    public BookTo get(int id) {
        log.info("get book {}", id);
        Book book = bookRepository.getExisted(id);
        return convertToDto(book);
    }

    /**
     * Retrieves all books filtered by the specified parameters.
     *
     * @param pageable        the pagination information
     * @param criteria        the search criteria
     * @return a list of BookTo DTOs representing all books filtered by the specified parameters
     */
    public Page<BookTo> getAll(Pageable pageable, BookSearchCriteriaTo criteria) {
        log.info("Retrieving books with params: {}", criteria.toString());
        Page<Book> page = bookRepository.findAll(pageable, criteria);
        List<BookTo> bookToList = page.getContent()
                .stream()
                .map(this::convertToDto)
                .toList();
        return new PageImpl<>(bookToList, pageable, page.getTotalElements());
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
        Book saved = bookRepository.save(convertFromDto(book));
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
        Book book = bookRepository.getExisted(id);
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
        bookRepository.deleteExisted(id);
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
                .publisherId(book.getPublisher().id())
                .build();
    }

    /**
     * Converts a BookTo DTO to a Book entity.
     *
     * @param bookTo the BookTo DTO to convert
     * @return the corresponding Book entity
     */
    private Book convertFromDto(BookTo bookTo) {
        Publisher publisher = publisherRepository.getExisted(bookTo.getPublisherId());
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
                .publisher(publisher)
                .build();
    }

    private String getFileName(String source) {
        String[] strings = source.split("/");
        return strings[strings.length - 1];
    }
}