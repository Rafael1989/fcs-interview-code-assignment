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
    @Transactional
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
    @Transactional
    void testGetSingleStoreEndpoint() {
        // Create store first
        Store store = new Store("GetSingle_" + System.currentTimeMillis(), 50);
        store.persist();

        given()
            .when().get("/store/" + store.id)
            .then()
            .statusCode(200)
            .body("id", equalTo(store.id.intValue()))
            .body("name", equalTo(store.name))
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
    @Transactional
    void testUpdateStoreEndpoint() {
        // Create store first
        Store store = new Store("UpdateTest_" + System.currentTimeMillis(), 50);
        store.persist();

        String newName = "Updated_" + System.currentTimeMillis();

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + newName + "\",\"quantityProductsInStock\":150}")
            .when().put("/store/" + store.id)
            .then()
            .statusCode(200)
            .body("name", equalTo(newName))
            .body("quantityProductsInStock", equalTo(150));
    }

    @Test
    @Transactional
    void testPatchStoreEndpoint() {
        // Create store first
        Store store = new Store("PatchTest_" + System.currentTimeMillis(), 50);
        store.persist();

        String newName = "Patched_" + System.currentTimeMillis();

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + newName + "\",\"quantityProductsInStock\":200}")
            .when().patch("/store/" + store.id)
            .then()
            .statusCode(200)
            .body("name", equalTo(newName))
            .body("quantityProductsInStock", equalTo(200));
    }

    @Test
    @Transactional
    void testDeleteStoreEndpoint() {
        // Create store first
        Store store = new Store("DeleteTest_" + System.currentTimeMillis(), 50);
        store.persist();

        given()
            .when().delete("/store/" + store.id)
            .then()
            .statusCode(204);

        // Verify it's deleted
        given()
            .when().get("/store/" + store.id)
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

