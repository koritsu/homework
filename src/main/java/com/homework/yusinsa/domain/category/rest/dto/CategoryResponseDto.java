package com.homework.yusinsa.domain.category.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryResponseDto {

    private Long id;

    private String name;

}