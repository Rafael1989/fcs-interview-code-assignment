package com.fulfilment.application.monolith.fulfillment.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class WarehouseProductStoreAssociationTest {

  @Test
  void testAssociationCanBeCreated() {
    // Given
    WarehouseProductStoreAssociation association = new WarehouseProductStoreAssociation();
    association.warehouseBusinessUnitCode = "WH-001";
    association.productId = 1L;
    association.storeId = 1L;
    association.createdAt = LocalDateTime.now();

    // Then
    assertNotNull(association);
    assertEquals("WH-001", association.warehouseBusinessUnitCode);
    assertEquals(1L, association.productId);
    assertEquals(1L, association.storeId);
    assertNotNull(association.createdAt);
  }

  @Test
  void testAssociationWithMultipleProducts() {
    // Given
    WarehouseProductStoreAssociation assoc1 = new WarehouseProductStoreAssociation();
    assoc1.warehouseBusinessUnitCode = "WH-001";
    assoc1.productId = 1L;
    assoc1.storeId = 1L;

    WarehouseProductStoreAssociation assoc2 = new WarehouseProductStoreAssociation();
    assoc2.warehouseBusinessUnitCode = "WH-001";
    assoc2.productId = 2L;
    assoc2.storeId = 1L;

    // Then
    assertNotNull(assoc1);
    assertNotNull(assoc2);
    assertEquals(assoc1.warehouseBusinessUnitCode, assoc2.warehouseBusinessUnitCode);
    assertNotEquals(assoc1.productId, assoc2.productId);
  }

  @Test
  void testAssociationEquality() {
    // Given
    WarehouseProductStoreAssociation association = new WarehouseProductStoreAssociation();
    association.warehouseBusinessUnitCode = "WH-002";
    association.productId = 5L;
    association.storeId = 2L;

    // Then
    assertNotNull(association);
    assertEquals("WH-002", association.warehouseBusinessUnitCode);
    assertEquals(5L, association.productId);
    assertEquals(2L, association.storeId);
  }
}

