package com.homework.yusinsa.domain.price.rest.controller;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static com.homework.yusinsa.common.Constant.CATEGORY_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PriceControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 20000;
    }

    @DisplayName("브랜드별 가장 저렴한 상품 가격 및 카테고리별 가격 조회")
    @Test
    public void testGetCheapestTotalCategoryPrice() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/prices/brands/cheapest-total-category-price")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.brandName", equalTo("D"))
                .body("data.categories", hasSize(8))
                .body("data.categories[0].categoryName", equalTo("상의"))
                .body("data.categories[0].price", equalTo(10100))
                .body("data.categories[1].categoryName", equalTo("아우터"))
                .body("data.categories[1].price", equalTo(5100))
                .body("data.categories[2].categoryName", equalTo("바지"))
                .body("data.categories[2].price", equalTo(3000))
                .body("data.categories[3].categoryName", equalTo("스니커즈"))
                .body("data.categories[3].price", equalTo(9500))
                .body("data.categories[4].categoryName", equalTo("가방"))
                .body("data.categories[4].price", equalTo(2500))
                .body("data.categories[5].categoryName", equalTo("모자"))
                .body("data.categories[5].price", equalTo(1500))
                .body("data.categories[6].categoryName", equalTo("양말"))
                .body("data.categories[6].price", equalTo(2400))
                .body("data.categories[7].categoryName", equalTo("액세서리"))
                .body("data.categories[7].price", equalTo(2000))
                .body("data.totalPrice", equalTo(36100))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("카테고리별 가장 저렴한 브랜드 및 가격 조회")
    @Test
    public void testGetCategoriesCheapestBrand() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/prices/categories/cheapest-brand")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.categories", hasSize(8))
                .body("data.categories[0].categoryName", equalTo("상의"))
                .body("data.categories[0].brandName", equalTo("C"))
                .body("data.categories[0].price", equalTo(10000))
                .body("data.totalPrice", equalTo(34100))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("카테고리 이름의 최저가, 최고가 브랜드 조회")
    @Test
    public void testGetPriceDetailsByCategoryName() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/prices/detail/categories/상의")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.categoryName", equalTo("상의"))
                .body("data.cheapestPriceBrands[0].brandName", equalTo("C"))
                .body("data.cheapestPriceBrands[0].price", equalTo(10000))
                .body("data.mostExpensivePriceBrands[0].brandName", equalTo("I"))
                .body("data.mostExpensivePriceBrands[0].price", equalTo(11400))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("카테고리 이름의 최저가, 최고가 브랜드 조회 실패- 존재하지 않는 카테고리")
    @Test
    public void testGetPriceDetailsByCategoryNameWithNonExsistName() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/prices/detail/categories/거의")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(CATEGORY_NOT_FOUND))
                .body("status", equalTo(404));
    }

}