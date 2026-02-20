package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class DbWarehouseTest {

  @Test
  void testDefaultConstructor() {
    // Given & When
    DbWarehouse warehouse = new DbWarehouse();

    // Then
    assertNotNull(warehouse);
  }

  @Test
  void testToWarehouseConversion() {
    // Given
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.id = 1L;
    dbWarehouse.businessUnitCode = "WH-001";
    dbWarehouse.location = "AMSTERDAM-001";
    dbWarehouse.capacity = 100;
    dbWarehouse.stock = 50;
    LocalDateTime createdDateTime = LocalDateTime.now().minusMonths(3);
    dbWarehouse.createdAt = createdDateTime;
    dbWarehouse.archivedAt = null;

    // When
    Warehouse result = dbWarehouse.toWarehouse();

    // Then
    assertNotNull(result);
    assertEquals("WH-001", result.businessUnitCode);
    assertEquals("AMSTERDAM-001", result.location);
    assertEquals(100, result.capacity);
    assertEquals(50, result.stock);
    assertEquals(createdDateTime, result.createdAt);
    assertNull(result.archivedAt);
  }

  @Test
  void testToWarehouseConversionWithArchivedDate() {
    // Given
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.businessUnitCode = "WH-002";
    dbWarehouse.location = "ZWOLLE-001";
    dbWarehouse.capacity = 80;
    dbWarehouse.stock = 30;
    LocalDateTime archivedDateTime = LocalDateTime.now();
    dbWarehouse.archivedAt = archivedDateTime;

    // When
    Warehouse result = dbWarehouse.toWarehouse();

    // Then
    assertNotNull(result);
    assertEquals("WH-002", result.businessUnitCode);
    assertNotNull(result.archivedAt);
    assertEquals(archivedDateTime, result.archivedAt);
  }

  @Test
  void testWarehouseFieldsSet() {
    // Given
    DbWarehouse warehouse = new DbWarehouse();
    warehouse.id = 999L;
    warehouse.businessUnitCode = "WH-MEGA";
    warehouse.location = "ROTTERDAM-001";
    warehouse.capacity = 500;
    warehouse.stock = 250;

    // When & Then
    assertEquals(999L, warehouse.id);
    assertEquals("WH-MEGA", warehouse.businessUnitCode);
    assertEquals("ROTTERDAM-001", warehouse.location);
    assertEquals(500, warehouse.capacity);
    assertEquals(250, warehouse.stock);
  }
}
