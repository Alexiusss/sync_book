package com.example.sync_book.web;

import com.example.sync_book.service.BookService;
import com.example.sync_book.to.BookSearchCriteriaTo;
import com.example.sync_book.to.BookTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * REST controller to handle CRUD operations with books
 */
@RestController
@RequestMapping(path = BookController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class BookController {
    public static final String REST_URL = "/api/v1/books";

    private final BookService bookService;

    @Operation(summary = "Get a book by its id")
    @GetMapping("/{id}")
    public ResponseEntity<BookTo> get(@PathVariable int id) {
        BookTo book = bookService.get(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Get a book by its name and file type")
    @GetMapping("/search")
    public ResponseEntity<BookTo> getByName(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "fileType") String fileType
    ) {
        BookTo book = bookService.getByNameAndFileType(name, fileType);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Return a list of books")
    @GetMapping
    public ResponseEntity<Page<BookTo>> getAll(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute @Parameter(description = "Criteria for searching books") BookSearchCriteriaTo criteria) {
        Page<BookTo> books = bookService.getAll(pageable, criteria);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Create a new book")
    @PostMapping
    public ResponseEntity<BookTo> create(@Valid @RequestBody BookTo book) {
        BookTo create = bookService.create(book);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(create.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(create);
    }

    @Operation(summary = "Update a book by its id")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody BookTo book) {
        bookService.update(id, book);
    }

    @Operation(summary = "Delete a book by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        bookService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}