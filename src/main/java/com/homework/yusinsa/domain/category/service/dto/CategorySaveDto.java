package com.homework.yusinsa.domain.category.service.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategorySaveDto {

    private String name;

}