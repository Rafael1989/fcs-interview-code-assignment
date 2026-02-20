package com.fulfilment.application.monolith.fulfillment.adapters.database;

import static org.junit.jupiter.api.Assertions.*;

import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class DbWarehouseProductStoreAssociationTest {

  @Test
  void testToAssociationConversion() {
    // Given
    DbWarehouseProductStoreAssociation dbAssociation = new DbWarehouseProductStoreAssociation();
    dbAssociation.id = 1L;
    dbAssociation.warehouseBusinessUnitCode = "WH-001";
    dbAssociation.productId = 10L;
    dbAssociation.storeId = 5L;
    LocalDateTime now = LocalDateTime.now();
    dbAssociation.createdAt = now;

    // When
    WarehouseProductStoreAssociation result = dbAssociation.toAssociation();

    // Then
    assertNotNull(result);
    assertEquals(1L, result.id);
    assertEquals("WH-001", result.warehouseBusinessUnitCode);
    assertEquals(10L, result.productId);
    assertEquals(5L, result.storeId);
    assertEquals(now, result.createdAt);
  }

  @Test
  void testDefaultConstructor() {
    // Given & When
    DbWarehouseProductStoreAssociation association = new DbWarehouseProductStoreAssociation();

    // Then
    assertNotNull(association);
  }

  @Test
  void testAssociationFieldsSet() {
    // Given
    DbWarehouseProductStoreAssociation association = new DbWarehouseProductStoreAssociation();
    association.id = 100L;
    association.warehouseBusinessUnitCode = "WH-LARGE";
    association.productId = 999L;
    association.storeId = 777L;

    // When & Then
    assertEquals(100L, association.id);
    assertEquals("WH-LARGE", association.warehouseBusinessUnitCode);
    assertEquals(999L, association.productId);
    assertEquals(777L, association.storeId);
  }
}
