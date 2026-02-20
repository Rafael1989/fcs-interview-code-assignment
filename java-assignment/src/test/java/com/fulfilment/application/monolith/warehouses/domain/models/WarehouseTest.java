package com.fulfilment.application.monolith.warehouses.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class WarehouseTest {

  @Test
  void testWarehouseCanBeCreated() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "WH-001";
    warehouse.location = "AMSTERDAM";
    warehouse.capacity = 100;
    warehouse.stock = 50;

    // Then
    assertNotNull(warehouse);
    assertEquals("WH-001", warehouse.businessUnitCode);
    assertEquals("AMSTERDAM", warehouse.location);
    assertEquals(100, warehouse.capacity);
    assertEquals(50, warehouse.stock);
    assertNull(warehouse.archivedAt);
  }

  @Test
  void testWarehouseWithArchivedDate() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "WH-002";
    warehouse.location = "ROTTERDAM";
    warehouse.capacity = 80;
    warehouse.stock = 40;
    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = LocalDateTime.now();

    // Then
    assertNotNull(warehouse);
    assertEquals("WH-002", warehouse.businessUnitCode);
    assertEquals("ROTTERDAM", warehouse.location);
    assertNotNull(warehouse.createdAt);
    assertNotNull(warehouse.archivedAt);
  }

  @Test
  void testWarehouseStockAndCapacity() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "WH-003";
    warehouse.capacity = 200;
    warehouse.stock = 150;

    // Then
    assertNotNull(warehouse);
    assertTrue(warehouse.stock <= warehouse.capacity);
    assertEquals(150, warehouse.stock);
    assertEquals(200, warehouse.capacity);
  }
}

