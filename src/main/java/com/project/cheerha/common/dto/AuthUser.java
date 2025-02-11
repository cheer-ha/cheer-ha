package com.project.cheerha.common.dto;

import com.project.cheerha.domain.user.entity.User.Role;

//로그인한 사용자 정보 담는 dto
public record AuthUser(Long id, Role userRole) {

}
