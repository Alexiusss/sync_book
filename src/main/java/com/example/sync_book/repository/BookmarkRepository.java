package com.example.sync_book.repository;

import com.example.sync_book.model.Bookmark;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends BaseRepository<Bookmark>, JpaSpecificationExecutor<Bookmark> {

    List<Bookmark> getAllByUserId(String userId);

    void deleteAllByUserId(String userId);

}