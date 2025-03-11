package com.project.cheerha.domain.elasticsearch.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReadJobOpeningElasticAutoRequestDto {

    @NotBlank
    private String searchTerm;
}
