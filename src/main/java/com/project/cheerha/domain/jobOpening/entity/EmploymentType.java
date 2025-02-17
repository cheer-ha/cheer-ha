package com.project.cheerha.domain.jobOpening.entity;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;

import java.util.Arrays;

public enum EmploymentType {

    무관, 고졸, 전문학사, 학사, 석사, 박사;

    public static EmploymentType toEnum(String employmentType) {
        return Arrays.stream(EmploymentType.values())
            .filter(r -> r.name().equalsIgnoreCase(employmentType))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ClientErrorCode.INVALID_ENUM_VALUE));
    }
}
