package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryQuery {

    /**
     * 사용자가 이미 특정 채용 공고를 북마크한 적이 있는지 확인하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 채용 공고의 ID
     * @return 해당 사용자가 해당 채용 공고를 북마크한 경우 true, 그렇지 않으면 false
     */
    boolean existsByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);

    /**
     * 사용자의 모든 북마크를 페이징 처리하여 조회하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @param pageable 페이징 처리 정보
     * @return 주어진 사용자에 대한 북마크 목록 (페이징 처리된 결과)
     */
    @EntityGraph(attributePaths = {
            "jobOpening",
            "jobOpening.jobOpeningKeywordList",
            "jobOpening.jobOpeningKeywordList.keyword"
    })
    Page<Bookmark> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자가 특정 채용 공고에 대한 북마크를 삭제하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 삭제할 채용 공고의 ID
     */
    void deleteByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);

    /**
     * 사용자가 저장한 북마크의 개수를 조회하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @return 해당 사용자가 저장한 북마크의 개수
     */
    long countByUserId(Long userId);

    /**
     * 사용자의 북마크 중 가장 오래된 북마크를 조회하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @return 가장 오래된 북마크 (ID가 가장 작은 북마크)
     */
    Bookmark findFirstByUserIdOrderByIdAsc(Long userId);
}
