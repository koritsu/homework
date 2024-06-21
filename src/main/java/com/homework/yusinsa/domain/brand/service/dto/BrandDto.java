package com.homework.yusinsa.domain.brand.service.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandDto {

    private Long id;

    private String name;

}