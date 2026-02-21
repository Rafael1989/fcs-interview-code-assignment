package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LegacyStoreManagerGateway
 *
 * Tests the integration point where Store changes are propagated to legacy systems
 */
@QuarkusTest
class LegacyStoreManagerGatewayTest {

  @Inject
  LegacyStoreManagerGateway gateway;

  private Store testStore;

  @BeforeEach
  @Transactional
  void setup() {
    // Create a test store
    testStore = new Store();
    testStore.name = "TestStore_" + System.currentTimeMillis();
    testStore.quantityProductsInStock = 100;
    testStore.persist();
  }

  @Test
  void testCreateStoreOnLegacySystemNotNull() {
    assertNotNull(gateway, "LegacyStoreManagerGateway should be injected");
  }

  @Test
  @Transactional
  void testCreateStoreOnLegacySystem() {
    // This method creates a temporary file as a mock of legacy system interaction
    // Should not throw any exception
    assertDoesNotThrow(() -> {
      gateway.createStoreOnLegacySystem(testStore);
    }, "createStoreOnLegacySystem should handle store creation");
  }

  @Test
  @Transactional
  void testUpdateStoreOnLegacySystem() {
    // Fetch fresh instance from database
    Store freshStore = Store.findById(testStore.id);
    assertNotNull(freshStore);

    // Update fresh store
    freshStore.quantityProductsInStock = 50;

    // This method should not throw any exception
    assertDoesNotThrow(() -> {
      gateway.updateStoreOnLegacySystem(freshStore);
    }, "updateStoreOnLegacySystem should handle store updates");
  }

  @Test
  @Transactional
  void testCreateStoreOnLegacySystemWithValidData() {
    Store newStore = new Store();
    newStore.name = "LegacyTest_" + System.currentTimeMillis();
    newStore.quantityProductsInStock = 250;
    newStore.persist();

    // Should complete without error
    assertDoesNotThrow(() -> {
      gateway.createStoreOnLegacySystem(newStore);
    });

    // Verify store still exists in database
    assertNotNull(Store.findById(newStore.id), "Store should still exist after legacy call");
  }

  @Test
  @Transactional
  void testUpdateStoreOnLegacySystemMultipleTimes() {
    // First update - fetch fresh instance
    Store freshStore1 = Store.findById(testStore.id);
    freshStore1.quantityProductsInStock = 75;
    assertDoesNotThrow(() -> gateway.updateStoreOnLegacySystem(freshStore1));

    // Second update - fetch fresh instance again
    Store freshStore2 = Store.findById(testStore.id);
    freshStore2.quantityProductsInStock = 125;
    assertDoesNotThrow(() -> gateway.updateStoreOnLegacySystem(freshStore2));

    // Verify final state
    Store retrieved = Store.findById(testStore.id);
    assertEquals(125, retrieved.quantityProductsInStock);
  }
}

