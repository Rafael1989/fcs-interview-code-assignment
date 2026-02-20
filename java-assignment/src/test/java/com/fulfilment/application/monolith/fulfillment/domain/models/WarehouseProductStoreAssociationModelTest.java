package com.fulfilment.application.monolith.fulfillment.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class WarehouseProductStoreAssociationModelTest {

  @Test
  void testAssociationDefaultFields() {
    // Given & When
    WarehouseProductStoreAssociation association = new WarehouseProductStoreAssociation();

    // Then
    assertNotNull(association);
    assertNull(association.id);
    assertNull(association.warehouseBusinessUnitCode);
    assertNull(association.productId);
    assertNull(association.storeId);
    assertNull(association.createdAt);
  }

  @Test
  void testAssociationFieldsSet() {
    // Given
    WarehouseProductStoreAssociation association = new WarehouseProductStoreAssociation();
    association.id = 1L;
    association.warehouseBusinessUnitCode = "WH-001";
    association.productId = 10L;
    association.storeId = 5L;
    LocalDateTime now = LocalDateTime.now();
    association.createdAt = now;

    // When & Then
    assertEquals(1L, association.id);
    assertEquals("WH-001", association.warehouseBusinessUnitCode);
    assertEquals(10L, association.productId);
    assertEquals(5L, association.storeId);
    assertEquals(now, association.createdAt);
  }

  @Test
  void testAssociationMultipleInstances() {
    // Given
    WarehouseProductStoreAssociation assoc1 = new WarehouseProductStoreAssociation();
    assoc1.id = 1L;
    assoc1.warehouseBusinessUnitCode = "WH-001";
    assoc1.productId = 1L;
    assoc1.storeId = 1L;

    WarehouseProductStoreAssociation assoc2 = new WarehouseProductStoreAssociation();
    assoc2.id = 2L;
    assoc2.warehouseBusinessUnitCode = "WH-002";
    assoc2.productId = 2L;
    assoc2.storeId = 2L;

    // When & Then
    assertNotEquals(assoc1.id, assoc2.id);
    assertNotEquals(assoc1.warehouseBusinessUnitCode, assoc2.warehouseBusinessUnitCode);
  }

  @Test
  void testAssociationWithCreatedAt() {
    // Given
    WarehouseProductStoreAssociation association = new WarehouseProductStoreAssociation();
    LocalDateTime createdDate = LocalDateTime.now();
    association.createdAt = createdDate;

    // When & Then
    assertNotNull(association.createdAt);
    assertEquals(createdDate, association.createdAt);
  }
}

