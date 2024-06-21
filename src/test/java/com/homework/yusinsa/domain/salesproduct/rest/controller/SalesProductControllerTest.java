package com.homework.yusinsa.domain.salesproduct.rest.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static com.homework.yusinsa.common.Constant.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesProductControllerTest {

    public final String uri = "/v1/sales-products";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 20000;

    }

    @DisplayName("상품 목록 조회")
    @Test
    @Order(0)
    void getSalesProducts() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data[0].id", equalTo(1))
                .body("data[0].category.id", equalTo(1))
                .body("data[0].category.name", equalTo("상의"))
        ;
    }

    @DisplayName("상품 조회")
    @Test
    @Order(1)
    void getSalesProduct() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri + "/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo(1))
                .body("data.category.id", equalTo(1))
                .body("data.category.name", equalTo("상의"))
        ;
    }

    @DisplayName("상품 생성")
    @Test
    @Order(2)
    void testCreateSalesProduct() {

        Response categoryResponse = given()
                .body("""
                        { "name": "Example Category" }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.name", equalTo("Example Category"))
                .body("message", equalTo("OK"))
                .extract()
                .response();

        Long categoryId = categoryResponse.jsonPath().getLong("data.id");

        Response brandResponse = given()
                .body("""
                        { "name": "Example Brand" }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/brands")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.name", equalTo("Example Brand"))
                .body("message", equalTo("OK"))
                .extract()
                .response();

        Long brandId = brandResponse.jsonPath().getLong("data.id");

        String requestBody = """
                {
                    "brand": {"id":%d},
                    "category": {"id":%d},
                    "price": 10000
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(String.format(requestBody, brandId, categoryId))
                .when()
                .post(uri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo(73))
                .body("data.brand.id", equalTo(brandId.intValue()))
                .body("data.brand.name", equalTo(brandResponse.jsonPath().getString("data.name")))
                .body("data.category.id", equalTo(categoryId.intValue()))
                .body("data.category.name", equalTo(categoryResponse.jsonPath().getString("data.name")))
                .body("data.price", equalTo(10000))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("상품 생성 - 중복된 상품으로 시도하여 실패")
    @Test
    @Order(3)
    void testCreateDuplicatedSalesProductFail() {

        Response categoryResponse = given()
                .body("""
                        { "name": "Example Category" }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        Long categoryId = categoryResponse.jsonPath().getLong("data.id");

        Response brandResponse = given()
                .body("""
                        { "name": "Example Brand" }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/brands")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        Long brandId = brandResponse.jsonPath().getLong("data.id");

        String requestBody = """
                {
                    "brand": {"id":%d},
                    "category": {"id":%d},
                    "price": 10000
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(String.format(requestBody, brandId, categoryId))
                .when()
                .post(uri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo(73))
                .body("data.brand.id", equalTo(brandId.intValue()))
                .body("data.brand.name", equalTo(brandResponse.jsonPath().getString("data.name")))
                .body("data.category.id", equalTo(categoryId.intValue()))
                .body("data.category.name", equalTo(categoryResponse.jsonPath().getString("data.name")))
                .body("data.price", equalTo(10000))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));

        given()
                .contentType(ContentType.JSON)
                .body(String.format(requestBody, brandId, categoryId))
                .when()
                .post(uri)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("status", equalTo(409));
    }

    @DisplayName("상품 수정 성공")
    @Test
    @Order(4)
    void updateSalesProduct() {
        String requestBody = """
                {
                    "brand": {"id":1},
                    "category": {"id":1},
                    "price": 9999
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo(1))
                .body("data.brand.id", equalTo(1))
                .body("data.brand.name", equalTo("A"))
                .body("data.category.id", equalTo(1))
                .body("data.category.name", equalTo("상의"))
                .body("data.price", equalTo(9999))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("상품 수정 - 중복된 브랜드와 카테고리로 시도하여 실패")
    @Test
    @Order(5)
    void updateSalesProductFailWhenBrandIdAndCategoryIdAlreadyExistsAndNotRequestedId() {
        String requestBody = """
                {
                    "brand": {"id": 1},
                    "category": {"id": 2},
                    "price": 9999
                }""";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/1")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo(String.format(SALES_PRODUCT_FORMAT_ALREADY_EXISTS, 1, 2)))
                .body("status", equalTo(409));
    }

    @DisplayName("상품 수정 - 존재하지 않는 브랜드로 시도하여 실패")
    @Test
    @Order(6)
    void updateSalesProductFailWhenBrandIdNotExists() {
        String requestBody = """
                {
                    "brand": {"id":100},
                    "category": {"id":2},
                    "price": 9999
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(BRAND_NOT_FOUND))
                .body("status", equalTo(404));
    }

    @DisplayName("상품 수정 - 존재하지 않는 카테고리로 시도하여 실패")
    @Test
    @Order(7)
    void updateSalesProductFailWhenCategoryIdNotExists() {
        String requestBody = """
                {
                    "brand": {"id":1},
                    "category": {"id":2000},
                    "price": 9999
                }""";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(CATEGORY_NOT_FOUND))
                .body("status", equalTo(404));
    }

    @DisplayName("상품 삭제 성공")
    @Test
    @Order(8)
    void deleteSalesProductSuccess() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(uri + "/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("상품 삭제 - 없는 아이디로 시도하여 실패")
    @Test
    @Order(9)
    void deleteSalesProductWithNoIdFail() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(uri + "/100")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(SALES_PRODUCT_NOT_FOUND))
                .body("status", equalTo(404));
    }

}