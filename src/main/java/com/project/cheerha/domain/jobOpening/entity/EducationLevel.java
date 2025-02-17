package com.project.cheerha.domain.jobOpening.entity;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;

import java.util.Arrays;

public enum EducationLevel {

    정규직, 계약직, 아르바이트, 인턴, 프리랜서;

    public static EducationLevel toEnum(String educationLevel) {
        return Arrays.stream(EducationLevel.values())
            .filter(r -> r.name().equalsIgnoreCase(educationLevel))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ClientErrorCode.INVALID_ENUM_VALUE));
    }
}
