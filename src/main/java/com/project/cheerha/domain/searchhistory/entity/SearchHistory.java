package com.project.cheerha.domain.searchhistory.entity;

import com.project.cheerha.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "search_history")
@EntityListeners(AuditingEntityListener.class)
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public static SearchHistory toEntity(User user, String name) {
        SearchHistory searchHistory = new SearchHistory();

        searchHistory.user = user;
        searchHistory.name = name;

        return searchHistory;
    }
}
