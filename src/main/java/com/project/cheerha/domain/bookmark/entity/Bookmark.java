package com.project.cheerha.domain.bookmark.entity;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bookmark", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "data_id"})}
)  // user_id와 data_id의 복합 유니크 제약)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_id")
    private Data data;

    public Bookmark(Data data) {
        this.data = data;
    }

    public Bookmark(User user, Data data) {
        this.user = user;
        this.data = data;
    }

    // userId로 User를 찾아서 Bookmark 엔티티 생성
    public static Bookmark toEntity(User user, Data data) {
        return new Bookmark(user, data);  // user와 data를 이용해 Bookmark 엔티티 생성
    }
}
