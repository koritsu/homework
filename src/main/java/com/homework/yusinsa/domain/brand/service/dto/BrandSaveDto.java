package com.homework.yusinsa.domain.brand.service.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandSaveDto {

    private String name;

}