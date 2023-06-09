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
                .put("/user")
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
                .put("/user")
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
                .put("/user")
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
                .put("/nearbyUser")
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
                .body("{\"userUUID\": \"AABC\", \"names\": [{\"foodName\": \"음식7\",\"amount\": \"30L\", \"bestBefore\": 1637711686,\"publicFood\": true}]}")
                .contentType(ContentType.JSON)
                .put("/food")
                .body().prettyPrint();

        var response = given()
                .body("{\"requestUUID\":\"AABC\", \"userUUID\": \"AABC\"}")
                .contentType(ContentType.JSON)
                .put("/food")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());
        Assertions.assertEquals("음식7", response.jsonPath().get("names[0].foodName"));
        Assertions.assertEquals("30L", response.jsonPath().get("names[0].amount"));
        Assertions.assertEquals(1637711686L,response.jsonPath().getLong("names[0].bestBefore"));
        Assertions.assertEquals(true, response.jsonPath().getBoolean("names[0].publicFood"));
    }

    @Test
    void createFoodAndResetByPost() {
        given()
                .body("{\"userUUID\": \"AABC\", \"names\": [{\"foodName\": \"음식7\",\"amount\": \"30L\", \"bestBefore\": 1637711686,\"publicFood\": true}]}")
                .contentType(ContentType.JSON)
                .post("/food")
                .body().prettyPrint();

        given()
                .body("{\"userUUID\": \"AABC\", \"names\": [{\"foodName\": \"sq\",\"amount\": \"15L\", \"bestBefore\": 1637711696,\"publicFood\": false}]}")
                .contentType(ContentType.JSON)
                .post("/food")
                .body().prettyPrint();

        var response = given()
                .body("{\"requestUUID\":\"AABC\", \"userUUID\": \"AABC\"}")
                .contentType(ContentType.JSON)
                .put("/food")
                .thenReturn();


        assertEquals("statusCode", 200, response.statusCode());
        Assertions.assertEquals("sq", response.jsonPath().get("names[0].foodName"));
        Assertions.assertEquals("15L", response.jsonPath().get("names[0].amount"));
        Assertions.assertEquals(1637711696L,response.jsonPath().getLong("names[0].bestBefore"));
        Assertions.assertEquals(false, response.jsonPath().getBoolean("names[0].publicFood"));
    }
}

