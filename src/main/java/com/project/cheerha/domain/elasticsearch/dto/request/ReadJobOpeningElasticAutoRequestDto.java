package com.project.cheerha.domain.elasticsearch.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReadJobOpeningElasticAutoRequestDto {

    private String searchTerm;
}
