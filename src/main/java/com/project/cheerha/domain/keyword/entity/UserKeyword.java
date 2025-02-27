package com.project.cheerha.domain.keyword.entity;

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
@Table(name = "user_keyword", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "keyword_id"})}
)
public class UserKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    public static UserKeyword toEntity(
        User user,
        Keyword keyword
    ) {
        UserKeyword userKeyword = new UserKeyword();
        userKeyword.user = user;
        userKeyword.keyword = keyword;

        return userKeyword;
    }
}
