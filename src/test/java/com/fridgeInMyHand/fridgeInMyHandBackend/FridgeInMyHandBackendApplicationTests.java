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

    @Test
    void createUserByLocationAndGetNearbyUser() {
        given()
                .body("{\"UUID\": \"abcdef\", \"lat\": 1.1, \"long\": 1.5}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userLocation")
                .body().prettyPrint();

        given()
                .body("{\"UUID\": \"aabc\", \"lat\": 1.2, \"long\": 1.9}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userLocation")
                .body().prettyPrint();

        given()
                .body("{\"UUID\": \"bbac\", \"lat\": 1.2, \"long\": 1.3}")
                .contentType(ContentType.JSON)
                .when()
                .post("/userLocation")
                .body().prettyPrint();

        var response = given()
                .body("{\"UUID\": \"unused\",\"lat\": 1.3,\"long\": 1.2,\"lat_limit\": 0.5,\"long_limit\": 0.5}")
                .contentType(ContentType.JSON)
                .get("/nearbyUser")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());

        Assertions.assertEquals(1.1f, (Float)response.jsonPath().get("[0].lat"), 0.1f);
        Assertions.assertEquals(1.5f, (Float)response.jsonPath().get("[0].long"), 0.1f);
        Assertions.assertEquals("abcdef", response.jsonPath().get("[0].UUID"));
        Assertions.assertEquals(1.2f, (Float)response.jsonPath().get("[1].lat"), 0.1f);
        Assertions.assertEquals(1.3f, (Float)response.jsonPath().get("[1].long"), 0.1f);
        Assertions.assertEquals("bbac", response.jsonPath().get("[1].UUID"));
    }
    @Test
    void testGetFoodMethod(){
        given()
                .body("{\"requestUUID\":\"AABC\", \"userUUID\": \"AABC\", \"names\": [{\"foodName\": \"음식1\",\"bestBefore\": \"2022-05-24\",\"quantity\": \"30L\",\"public\": true}]}")
                .contentType(ContentType.JSON)
                .when()
                .post("/foods")
                .body().prettyPrint();
        var response = given()
                .body("{\"requestUUID\":\"AABC\", \"userUUID\": \"AABC\"}")
                .contentType(ContentType.JSON)
                .get("/food")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());
        Assertions.assertEquals("음식1", response.jsonPath().get("names[0].name"));
        Assertions.assertEquals("2022-05-24",response.jsonPath().get("names[0].bestBefore"));


    }
}
