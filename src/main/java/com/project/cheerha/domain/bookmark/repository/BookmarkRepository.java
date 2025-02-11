package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
