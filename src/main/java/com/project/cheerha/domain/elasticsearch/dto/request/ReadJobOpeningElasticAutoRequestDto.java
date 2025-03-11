package com.project.cheerha.domain.elasticsearch.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReadJobOpeningElasticAutoRequestDto {

    @NotBlank(message = "검색어는 비워둘 수 없습니다.")
    @Pattern(regexp = ".*\\S.*", message = "검색어는 공백만 포함할 수 없습니다.")
    private String searchTerm;
}
