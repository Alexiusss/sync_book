package com.example.sync_book.service;

import com.example.sync_book.model.Publisher;
import com.example.sync_book.repository.PublisherRepository;
import com.example.sync_book.to.PublisherTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.sync_book.util.ValidationUtil.assureIdConsistent;
import static com.example.sync_book.util.ValidationUtil.checkNew;

/**
 * Service class for managing publishers.
 * Provides methods to create, update, delete, and fetch publishers.
 */
@Service
@AllArgsConstructor
@Slf4j
public class PublisherService {

    private final PublisherRepository repository;

    /**
     * Retrieves a publisher by its ID.
     *
     * @param id the ID of the publisher to retrieve
     * @return the PublisherTo DTO of the retrieved publisher
     */
    public PublisherTo get(int id) {
        log.info("get publisher {}", id);
        Publisher publisher = repository.getExisted(id);
        return convertToDto(publisher);
    }

    /**
     * Retrieves all publishers.
     *
     * @return a list of PublisherTo DTOs representing all publishers
     */
    public List<PublisherTo> getAll() {
        log.info("get all publishers");
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new publisher.
     *
     * @param publisher the PublisherTo DTO of the publisher to create
     * @return the PublisherTo DTO of the created publisher
     */
    @Transactional
    public PublisherTo create(PublisherTo publisher) {
        Assert.notNull(publisher, "publisher must not be null");
        checkNew(publisher);
        Publisher saved = repository.save(convertFromDto(publisher));
        log.info("create new publisher {}", saved);
        return convertToDto(saved);
    }

    /**
     * Updates an existing publisher.
     *
     * @param id the ID of the publisher to update
     * @param publisherTo the PublisherTo DTO with updated data
     */
    @Transactional
    public void update(int id, PublisherTo publisherTo) {
        assureIdConsistent(publisherTo, id);
        log.info("update publisher {}", id);
        Publisher publisher = repository.getExisted(id);
        publisher.setName(publisherTo.getName());
        publisher.setFoundationYear(publisherTo.getFoundationYear());
        publisher.setCity(publisherTo.getCity());
    }

    /**
     * Deletes a publisher by its ID.
     *
     * @param id the ID of the publisher to delete
     */
    @Transactional
    public void delete(int id) {
        log.info("delete publisher {}", id);
        repository.deleteExisted(id);
    }

    /**
     * Converts a Publisher entity to a PublisherTo DTO.
     *
     * @param publisher the Publisher entity to convert
     * @return the corresponding PublisherTo DTO
     */
    private PublisherTo convertToDto(Publisher publisher) {
        return new PublisherTo(publisher.getId(), publisher.getName(), publisher.getFoundationYear(), publisher.getCity());
    }

    /**
     * Converts a PublisherTo DTO to a Publisher entity.
     *
     * @param publisherTo the PublisherTo DTO to convert
     * @return the corresponding Publisher entity
     */
    private Publisher convertFromDto(PublisherTo publisherTo) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherTo.getId());
        publisher.setName(publisherTo.getName());
        publisher.setFoundationYear(publisherTo.getFoundationYear());
        publisher.setCity(publisherTo.getCity());
        return publisher;
    }
}