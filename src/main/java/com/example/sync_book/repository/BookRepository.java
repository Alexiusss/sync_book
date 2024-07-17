package com.example.sync_book.repository;

import com.example.sync_book.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends BaseRepository<Book> {
    @EntityGraph(attributePaths = "publisher", type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT b FROM Book b " +
            "WHERE lower(b.genre) LIKE concat('%', COALESCE(NULLIF(lower(:genre), ''), '%')) " +
            "AND lower(b.name) LIKE concat('%', COALESCE(NULLIF(lower(:name), ''), '%')) " +
            "AND lower(b.author) LIKE concat('%', COALESCE(NULLIF(lower(:author), ''), '%')) " +
            "AND (:year IS NULL OR :year = -1 OR b.publicationYear = :year) ")
    List<Book> findAll(@Param("author") String author, @Param("name") String name, @Param("genre") String genre, @Param("year") Integer year);
}