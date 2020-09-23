package com.example.test.service;

import com.example.test.Application;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public class DeviceRestServiceTest {

    @Test
    public void verifyClaimSessionWithValidRequest() {
        final SessionHandle sessionHandle = given()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body("{\"deviceId\": 1}")
                .post("/devices/claim")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().response().as(SessionHandle.class);
        assertThat(sessionHandle.getDeviceId(), is(1L));
        free(sessionHandle);
    }

    @Test
    public void verifyClaimSessionWithValidRequestNoResult() {
        RestAssured.given()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body("{\"deviceId\": 5}")
                .post("/devices/claim")
                .then()
                .log().ifValidationFails()
                .statusCode(204);
    }

    @Test
    public void verifyClaimSessionWithInvalidRequest() {
        RestAssured.given()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body("{}")
                .post("/devices/claim")
                .then()
                .log().ifValidationFails()
                .statusCode(400);
    }

    private static void free(SessionHandle sessionHandle) {
        given()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .delete("/devices/free/" + sessionHandle.getSessionId())
                .then()
                .statusCode(200);
    }

}
