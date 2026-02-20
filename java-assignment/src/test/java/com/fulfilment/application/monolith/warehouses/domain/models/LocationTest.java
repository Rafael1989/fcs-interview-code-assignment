package com.fulfilment.application.monolith.warehouses.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LocationTest {

  @Test
  void testLocationConstructor() {
    // Given & When
    Location location = new Location("AMSTERDAM-001", 5, 100);

    // Then
    assertNotNull(location);
    assertEquals("AMSTERDAM-001", location.identification);
    assertEquals(5, location.maxNumberOfWarehouses);
    assertEquals(100, location.maxCapacity);
  }

  @Test
  void testLocationWithDifferentValues() {
    // Given & When
    Location zwolle = new Location("ZWOLLE-001", 1, 40);

    // Then
    assertEquals("ZWOLLE-001", zwolle.identification);
    assertEquals(1, zwolle.maxNumberOfWarehouses);
    assertEquals(40, zwolle.maxCapacity);
  }

  @Test
  void testLocationMaxWarehousesProperty() {
    // Given
    Location location = new Location("TEST-001", 10, 500);

    // When & Then
    assertEquals(10, location.maxNumberOfWarehouses);
    assertTrue(location.maxNumberOfWarehouses > 0);
  }

  @Test
  void testLocationMaxCapacityProperty() {
    // Given
    Location location = new Location("TEST-002", 3, 250);

    // When & Then
    assertEquals(250, location.maxCapacity);
    assertTrue(location.maxCapacity >= 100);
  }

  @Test
  void testMultipleLocationInstances() {
    // Given
    Location amst = new Location("AMSTERDAM-001", 5, 100);
    Location zwol = new Location("ZWOLLE-001", 1, 40);
    Location rott = new Location("ROTTERDAM-001", 3, 150);

    // When & Then
    assertEquals("AMSTERDAM-001", amst.identification);
    assertEquals("ZWOLLE-001", zwol.identification);
    assertEquals("ROTTERDAM-001", rott.identification);
    assertNotEquals(amst.maxCapacity, zwol.maxCapacity);
  }
}
