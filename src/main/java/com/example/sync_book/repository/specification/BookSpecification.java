package com.example.sync_book.repository.specification;

import com.example.sync_book.model.Book;
import com.example.sync_book.to.BookSearchCriteriaTo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BookSpecification implements Specification<Book> {

    private final BookSearchCriteriaTo criteria;

    @Override
    public Predicate toPredicate(@NotNull Root<Book> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        addPredicateIfValueLike(predicates, root, builder, "genre", criteria.genre());
        addPredicateIfValueLike(predicates, root, builder, "name", criteria.name());
        addPredicateIfValueLike(predicates, root, builder, "author", criteria.author());
        addPredicateIfValueEquals(predicates, root, builder, "language", criteria.language());
        addPredicateIfValueEquals(predicates, root, builder, "narrator", criteria.narrator());
        addPredicateIfValueEquals(predicates, root, builder, "translator", criteria.translator());
        addNumberPredicate(predicates, root, builder, "publicationYear", criteria.publicationYear());
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private void addPredicateIfValueLike(List<Predicate> predicates, Root<Book> root, CriteriaBuilder builder, String attribute, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get(attribute)), "%" + value.toLowerCase() + "%"));
        }
    }

    private void addNumberPredicate(List<Predicate> predicates, Root<Book> root, CriteriaBuilder builder, String attribute, Integer number) {
        if (number != null && number != -1) {
            predicates.add(builder.equal(root.get(attribute), number));
        }
    }

    private void addPredicateIfValueEquals(List<Predicate> predicates, Root<Book> root, CriteriaBuilder builder, String attribute, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(builder.equal(builder.lower(root.get(attribute)), value.toLowerCase()));
        }
    }
}