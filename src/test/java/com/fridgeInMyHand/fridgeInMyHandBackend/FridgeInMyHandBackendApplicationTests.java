package com.fridgeInMyHand.fridgeInMyHandBackend;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FridgeInMyHandBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void createUserByLocationAndGetUser() {
        given()
                .body("{\"UUID\": \"abcdef\", \"lat\": 1.1, \"long\": 1.5}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userLocation")
                .body().prettyPrint();

        var response = given()
                .body("{\"UUID\": \"abcdef\"}")
                .contentType(ContentType.JSON)
                .get("/user")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());
        Assertions.assertEquals(1.1f, (Float)response.jsonPath().get("lat"), 0.1f);
        Assertions.assertEquals(1.5f, (Float)response.jsonPath().get("long"), 0.1f);
        assertEquals("lat", null, response.jsonPath().get("url"));
    }

    @Test
    void createUserByURLAndGetUser() {
        given()
                .body("{\"UUID\": \"abcdef\", \"url\": \"https://blahblah.com\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userURL")
                .body().prettyPrint();

        var response = given()
                .body("{\"UUID\": \"abcdef\"}")
                .contentType(ContentType.JSON)
                .get("/user")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());
        Assertions.assertEquals((Double)null, response.jsonPath().get("lat"));
        Assertions.assertEquals((Double)null, response.jsonPath().get("long"));
        assertEquals("lat", "https://blahblah.com", response.jsonPath().get("url"));
    }

    @Test
    void createUserByURLWithLocationAndGetUser() {
        given()
                .body("{\"UUID\": \"abcdef\", \"url\": \"https://blahblah.com\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userURL")
                .body().prettyPrint();

        given()
                .body("{\"UUID\": \"abcdef\", \"lat\": 1.1, \"long\": 1.5}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userLocation")
                .body().prettyPrint();

        var response = given()
                .body("{\"UUID\": \"abcdef\"}")
                .contentType(ContentType.JSON)
                .get("/user")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());
        Assertions.assertEquals(1.1f, (Float)response.jsonPath().get("lat"), 0.1f);
        Assertions.assertEquals(1.5f, (Float)response.jsonPath().get("long"), 0.1f);
        assertEquals("lat", "https://blahblah.com", response.jsonPath().get("url"));
    }
}
