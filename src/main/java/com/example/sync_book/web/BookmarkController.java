package com.example.sync_book.web;

import com.example.sync_book.service.BookmarkService;
import com.example.sync_book.to.BookmarkTo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = BookmarkController.REST_URL, produces = APPLICATION_JSON_VALUE)
public class BookmarkController {
    public static final String REST_URL = "/api/v1/bookmarks";

    private final BookmarkService service;

    @Operation(summary = "Create a new bookmark")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookmarkTo> create(@Valid @RequestBody BookmarkTo bookMark) {
        BookmarkTo created = service.create(bookMark);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Update a bookmark by its id")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody BookmarkTo bookMark) {
        service.update(id, bookMark);
    }

    @Operation(summary = "Get a bookmark by its id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<BookmarkTo> getById(@PathVariable int id) {
        BookmarkTo bookmark = service.getById(id);
        return ResponseEntity.ok(bookmark);
    }


    @Operation(summary = "Return a list of bookmarks by user id")
    @GetMapping(path = "/user/{userId}")
    public ResponseEntity<List<BookmarkTo>> getAllByUserId(@PathVariable String userId) {
        List<BookmarkTo> bookmarks = service.getAllByUserId(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @Operation(summary = "Delete a bookmark by its id")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        service.delete(id);
    }

    @Operation(summary = "Delete all bookmarks by user id")
    @DeleteMapping(path = "/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByUserId(@PathVariable String userId) {
        service.deleteAllByUserId(userId);
    }
}