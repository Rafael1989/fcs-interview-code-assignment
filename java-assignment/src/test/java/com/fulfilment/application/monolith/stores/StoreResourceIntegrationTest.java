package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * REST Integration tests for StoreResource
 * These tests actually call HTTP endpoints to ensure real code execution
 */
@QuarkusTest
class StoreResourceIntegrationTest {

    @AfterEach
    @Transactional
    void cleanup() {
        Store.deleteAll();
    }

    @Test
    void testGetAllStoresEndpoint() {
        given()
            .when().get("/store")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testCreateStoreEndpoint() {
        String storeName = "IntegrationStore_" + System.currentTimeMillis();

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + storeName + "\",\"quantityProductsInStock\":100}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .body("name", equalTo(storeName))
            .body("quantityProductsInStock", equalTo(100));
    }

    @Test
    void testGetSingleStoreEndpoint() {
        // Create store via REST API first
        String storeName = "GetSingle_" + System.currentTimeMillis();

        String response = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + storeName + "\",\"quantityProductsInStock\":50}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .extract().path("id").toString();

        Long storeId = Long.parseLong(response);

        given()
            .when().get("/store/" + storeId)
            .then()
            .statusCode(200)
            .body("id", equalTo(storeId.intValue()))
            .body("name", equalTo(storeName))
            .body("quantityProductsInStock", equalTo(50));
    }

    @Test
    void testGetNonExistentStoreReturns404() {
        given()
            .when().get("/store/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testUpdateStoreEndpoint() {
        // Create store via REST API first
        String storeName = "UpdateTest_" + System.currentTimeMillis();

        String response = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + storeName + "\",\"quantityProductsInStock\":50}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .extract().path("id").toString();

        Long storeId = Long.parseLong(response);

        String newName = "Updated_" + System.currentTimeMillis();

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + newName + "\",\"quantityProductsInStock\":150}")
            .when().put("/store/" + storeId)
            .then()
            .statusCode(200)
            .body("name", equalTo(newName))
            .body("quantityProductsInStock", equalTo(150));
    }

    @Test
    void testPatchStoreEndpoint() {
        // Create store via REST API first
        String storeName = "PatchTest_" + System.currentTimeMillis();

        String response = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + storeName + "\",\"quantityProductsInStock\":50}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .extract().path("id").toString();

        Long storeId = Long.parseLong(response);

        String newName = "Patched_" + System.currentTimeMillis();

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + newName + "\",\"quantityProductsInStock\":200}")
            .when().patch("/store/" + storeId)
            .then()
            .statusCode(200)
            .body("name", equalTo(newName))
            .body("quantityProductsInStock", equalTo(200));
    }

    @Test
    void testDeleteStoreEndpoint() {
        // Create store via REST API first
        String storeName = "DeleteTest_" + System.currentTimeMillis();

        String response = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + storeName + "\",\"quantityProductsInStock\":50}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .extract().path("id").toString();

        Long storeId = Long.parseLong(response);

        given()
            .when().delete("/store/" + storeId)
            .then()
            .statusCode(204);

        // Verify it's deleted
        given()
            .when().get("/store/" + storeId)
            .then()
            .statusCode(404);
    }

    @Test
    void testCreateStoreWithIdShouldFail() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"id\":123,\"name\":\"InvalidStore\",\"quantityProductsInStock\":100}")
            .when().post("/store")
            .then()
            .statusCode(422);
    }

    @Test
    void testCreateStoreWithoutNameShouldFail() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"quantityProductsInStock\":100}")
            .when().post("/store")
            .then()
            .statusCode(422);
    }

    @Test
    void testCreateStoreWithNegativeStockShouldFail() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"NegativeStore\",\"quantityProductsInStock\":-10}")
            .when().post("/store")
            .then()
            .statusCode(422);
    }
}

