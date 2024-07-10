package com.homework.template.domain.brand.rest.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static com.homework.template.common.Constant.BRAND_NOT_FOUND;
import static com.homework.template.common.Constant.DATABASE_INTEGRITY_CONSTRAINT_VIOLATION;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BrandControllerTest {

    public final String uri = "/v1/brands";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 20000;
    }

    @DisplayName("브랜드 목록 조회 - 성공")
    @Test
    @Order(0)
    void getBrands() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.", hasSize(9))
                .body("data[0].id", equalTo(1))
                .body("data[0].name", equalTo("A"))
        ;
    }

    @DisplayName("브랜드 조회 - 성공")
    @Test
    @Order(1)
    void getBrand() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri + "/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo(1))
                .body("data.name", equalTo("A"))
        ;
    }

    @DisplayName("브랜드 생성 - 성공")
    @Test
    @Order(2)
    void testCreateBrand() {

        given()
                .body("""
                        { "name": "Example Category" }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post(uri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.name", equalTo("Example Category"))
                .body("message", equalTo("OK"));

    }

    @DisplayName("브랜드 생성 - 실패(중복된 상품으로 시도)")
    @Test
    @Order(3)
    void testCreateDuplicatedBrandFail() {

        var requestBody = """
                { "name": "Example Category" }
                """;

        given()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .when()
                .post(uri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.name", equalTo("Example Category"))
                .body("message", equalTo("OK"));
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(uri)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo(DATABASE_INTEGRITY_CONSTRAINT_VIOLATION))
                .body("status", equalTo(409));
    }

    @DisplayName("브랜드 수정 - 성공")
    @Test
    @Order(4)
    void updateBrand() {
        String requestBody = """
                {
                    "id": 1,
                    "name": "updated Brand"
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo(1))
                .body("data.name", equalTo("updated Brand"))
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("브랜드 수정 - 실패(중복된 이름으로 시도)")
    @Test
    @Order(5)
    void updateBrandFailWhenBrandIdAlreadyExistsAndNotRequestedId() {
        String requestBody = """
                {
                    "name": "A"
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/2")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo(DATABASE_INTEGRITY_CONSTRAINT_VIOLATION))
                .body("status", equalTo(409));
    }

    @DisplayName("브랜드 수정 - 실패(없는 아이디로 시도)")
    @Test
    @Order(6)
    void updateBrandFailWhenIdNotExists() {
        String requestBody = """
                {
                    "name": "상의"
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(uri + "/1000")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(BRAND_NOT_FOUND))
                .body("status", equalTo(404));
    }


    @DisplayName("브랜드 삭제 성공")
    @Test
    @Order(8)
    void deleteBrandSuccess() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(uri + "/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo("OK"))
                .body("status", equalTo(200));
    }

    @DisplayName("브랜드 삭제 - 실패 (존재하지 않는 아이디로 삭제)")
    @Test
    @Order(9)
    void deleteBrandWithNoIdFail() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(uri + "/100")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(BRAND_NOT_FOUND))
                .body("status", equalTo(404));
    }

}