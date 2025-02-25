package com.project.cheerha.domain.jobopening.entity;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;

import java.util.Arrays;

public enum EmploymentType {

    정규직, 계약직, 아르바이트, 인턴, 프리랜서;

    public static EmploymentType toEnum(String employmentType) {
        if (employmentType == null || employmentType.isBlank()) {
            return null;
        }

        return Arrays.stream(EmploymentType.values())
                .filter(r -> r.name().equalsIgnoreCase(employmentType))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ClientErrorCode.INVALID_ENUM_VALUE));
    }
}
