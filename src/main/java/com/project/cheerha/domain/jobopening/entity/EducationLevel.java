package com.project.cheerha.domain.jobopening.entity;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;

import java.util.Arrays;

public enum EducationLevel {

    무관, 고졸, 전문학사, 학사, 석사, 박사;

    public static EducationLevel toEnum(String educationLevel) {
        if (educationLevel == null || educationLevel.isBlank()) {
            return null;
        }

        return Arrays.stream(EducationLevel.values())
                .filter(r -> r.name().equalsIgnoreCase(educationLevel))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ClientErrorCode.INVALID_ENUM_VALUE));
    }
}

