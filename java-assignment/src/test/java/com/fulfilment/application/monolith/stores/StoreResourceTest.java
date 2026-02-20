package com.fulfilment.application.monolith.stores;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@ExtendWith({})
public class StoreResourceTest {

  @Test
  void testStoreEntityCanBeCreated() {
    // Given
    Store store = new Store("TEST_STORE");

    // Then
    assertNotNull(store);
    assertEquals("TEST_STORE", store.name);
    assertEquals(0, store.quantityProductsInStock);
  }

  @Test
  void testStoreEntityWithDefaultConstructor() {
    // Given
    Store store = new Store();

    // Then
    assertNotNull(store);
    assertNull(store.name);
    assertEquals(0, store.quantityProductsInStock);
  }

  @Test
  void testStoreEntityWithNameAndQuantity() {
    // Given
    Store store = new Store("STORE_A");
    store.quantityProductsInStock = 100;

    // Then
    assertNotNull(store);
    assertEquals("STORE_A", store.name);
    assertEquals(100, store.quantityProductsInStock);
  }
}

