package com.example.sync_book.service;

import com.example.sync_book.model.Bookmark;
import com.example.sync_book.repository.BookmarkRepository;
import com.example.sync_book.to.BookmarkTo;
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
 * Service class for managing bookmarks.
 * Provides methods to create, update, delete, and fetch bookmarks.
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookmarkService {

    @Autowired
    BookmarkRepository repository;

    /**
     * Creates a new bookmark.
     *
     * @param bookmark the BookmarkTo DTO of the bookmark to create
     * @return the BookmarkTo DTO of the created bookmark
     */
    @Transactional
    public BookmarkTo create(BookmarkTo bookmark) {
        Assert.notNull(bookmark, "bookmark must not be null");
        checkNew(bookmark);
        Bookmark saved = repository.save(convertFromDto(bookmark));
        log.info("create new bookmark {}", saved);
        return convertToDto(saved);
    }

    /**
     * Updates an existing bookmark.
     *
     * @param id         the ID of the bookmark to update
     * @param bookmarkTo the BookmarkTo DTO with updated data
     */
    @Transactional
    public void update(int id, BookmarkTo bookmarkTo) {
        assureIdConsistent(bookmarkTo, id);
        log.info("update bookmark {}", id);
        Bookmark bookmark = repository.getExisted(id);
        bookmark.setLastPosition(bookmarkTo.getLastPosition());
    }

    /**
     * Retrieves a bookmark by its ID.
     *
     * @param id the ID of the bookmark to retrieve
     * @return the BookmarkTo DTO of the retrieved bookmark
     */
    public BookmarkTo getById(int id) {
        log.info("get bookmark {}", id);
        Bookmark bookmark = repository.getExisted(id);
        return convertToDto(bookmark);
    }

    /**
     * Retrieves all bookmarks by user id.
     *
     * @return a list of BookmarkTo DTOs representing all bookmarks
     */
    public List<BookmarkTo> getAllByUserId(String userId) {
        log.info("get all bookmarks for user {}", userId);
        return repository.getAllByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete all bookmarks by user ID.
     *
     * @param userId the ID of the user
     */
    @Transactional
    public void deleteAllByUserId(String userId) {
        log.info("delete all bookmarks for user {}", userId);
        repository.deleteAllByUserId(userId);
    }

    /**
     * Deletes a bookmark by its ID.
     *
     * @param id the ID of the bookmark to delete
     */
    @Transactional
    public void delete(int id) {
        log.info("delete bookmark {}", id);
        repository.deleteExisted(id);
    }

    /**
     * Converts a BookmarkTo DTO to a Bookmark entity.
     *
     * @param bookmarkTo the BookmarkTo DTO to convert
     * @return the corresponding Bookmark entity
     */
    private Bookmark convertFromDto(BookmarkTo bookmarkTo) {
        return new Bookmark(bookmarkTo.getUserId(), bookmarkTo.getBookId(), bookmarkTo.getLastPosition());
    }

    /**
     * Converts a Bookmark entity to a BookmarkTo DTO.
     *
     * @param bookmark the Bookmark entity to convert
     * @return the corresponding BookmarkTo DTO
     */
    private BookmarkTo convertToDto(Bookmark bookmark) {
        return new BookmarkTo(bookmark.id(), bookmark.getUserId(), bookmark.getBookId(), bookmark.getLastPosition());
    }
}