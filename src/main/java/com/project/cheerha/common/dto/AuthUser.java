package com.project.cheerha.common.dto;

import com.project.cheerha.domain.user.entity.Role;

/**
 * 현재 사용자의 정보를 담는 DTO
 * @param id 현재 사용자의 식별자
 * @param role 현재 사용자의 권한정보
 */
public record AuthUser(Long id, Role role) {

}
