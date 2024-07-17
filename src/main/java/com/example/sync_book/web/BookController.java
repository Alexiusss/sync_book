package com.example.sync_book.web;

import com.example.sync_book.service.BookService;
import com.example.sync_book.to.BookTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Operation(summary = "Return a list of books")
    @GetMapping
    public ResponseEntity<Page<BookTo>> getAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) @Parameter(description = "Author's name") String author,
            @RequestParam(required = false) @Parameter(description = "Book's name") String name,
            @RequestParam(required = false) @Parameter(description = "Book's genre") String genre,
            @RequestParam(required = false) @Parameter(description = "Publication year") Integer publicationYear
    ) {
        Page<BookTo> books = bookService.getAll(PageRequest.of(page, size), author, name, genre, publicationYear);
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