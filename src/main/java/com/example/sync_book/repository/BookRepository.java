package com.example.sync_book.repository;

import com.example.sync_book.model.Book;
import com.example.sync_book.model.FileType;
import com.example.sync_book.to.BookSearchCriteriaTo;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends BaseRepository<Book>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = "publisher", type = EntityGraph.EntityGraphType.LOAD)
    Book findByNameAndFileType(@NotBlank String name, @NotNull FileType fileType);

    @EntityGraph(attributePaths = "publisher", type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT b FROM Book b " +
            "WHERE lower(b.genre) LIKE concat('%', COALESCE(NULLIF(lower(:#{#criteria.genre}), ''), '%')) " +
            "AND lower(b.name) LIKE concat('%', COALESCE(NULLIF(lower(:#{#criteria.name}), ''), '%')) " +
            "AND lower(b.author) LIKE concat('%', COALESCE(NULLIF(lower(:#{#criteria.author}), ''), '%')) " +
            "AND (:#{#criteria.publicationYear} IS NULL OR :#{#criteria.publicationYear} = -1 OR b.publicationYear = :#{#criteria.publicationYear}) ")
    Page<Book> findAll(Pageable pageable, @Param("criteria") BookSearchCriteriaTo criteria);

    @Override
    @EntityGraph(attributePaths = "publisher", type = EntityGraph.EntityGraphType.LOAD)
    @NotNull
    Page<Book> findAll(@NotNull Specification<Book> specification, @NotNull Pageable pageable);

    @EntityGraph(attributePaths = "publisher", type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findAllByPublisherId(Pageable pageable, int publisherId);
}