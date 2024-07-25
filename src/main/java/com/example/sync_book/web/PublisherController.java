package com.example.sync_book.web;

import com.example.sync_book.service.BookService;
import com.example.sync_book.service.PublisherService;
import com.example.sync_book.to.BookTo;
import com.example.sync_book.to.PublisherTo;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller to handle CRUD operations with publishers
 */
@RestController
@RequestMapping(path = PublisherController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PublisherController {
    public static final String REST_URL = "/api/v1/publishers";

    private final PublisherService publisherService;
    private final BookService bookServiceService;

    @Operation(summary = "Get a publisher by its id")
    @GetMapping("/{id}")
    public ResponseEntity<PublisherTo> get(@PathVariable int id) {
        PublisherTo publisher = publisherService.get(id);
        return ResponseEntity.ok(publisher);
    }

    @Operation(summary = "Return a list of publishers")
    @GetMapping
    public ResponseEntity<List<PublisherTo>> getAll() {
        List<PublisherTo> publishers = publisherService.getAll();
        return ResponseEntity.ok(publishers);
    }

    @Operation(summary = "Create a new publisher")
    @PostMapping
    public ResponseEntity<PublisherTo> create(@Valid @RequestBody PublisherTo publisher) {
        PublisherTo create = publisherService.create(publisher);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(create.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(create);
    }

    @Operation(summary = "Update a publisher by its id")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody PublisherTo publisher) {
        publisherService.update(id, publisher);
    }

    @Operation(summary = "Delete a publisher by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        publisherService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Return a list of books by publisher id")
    @GetMapping("/{id}/books")
    public ResponseEntity<Page<BookTo>> getAllBooks(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable int id) {
        Page<BookTo> books = bookServiceService.getAllByPublisherId(pageable, id);
        return ResponseEntity.ok(books);
    }
}