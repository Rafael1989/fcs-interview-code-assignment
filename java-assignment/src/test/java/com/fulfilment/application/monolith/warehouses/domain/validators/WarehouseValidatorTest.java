package com.fulfilment.application.monolith.warehouses.domain.validators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WarehouseValidatorTest {

  @Mock private WarehouseStore warehouseStore;
  @Mock private LocationResolver locationResolver;

  private WarehouseValidator validator;

  @BeforeEach
  void setup() {
    validator = new WarehouseValidator();
    validator.warehouseStore = warehouseStore;
    validator.locationResolver = locationResolver;
  }

  // ==================== CREATE VALIDATIONS ====================

  @Test
  void testValidateForCreateSuccessfully() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    Location location = new Location("AMSTERDAM-001", 5, 200);

    when(locationResolver.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));
    when(warehouseStore.getAll()).thenReturn(java.util.List.of());

    // When & Then - should not throw
    assertDoesNotThrow(() -> validator.validateForCreate(warehouse));
  }

  @Test
  void testValidateForCreateFailsWhenBusinessUnitCodeExists() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "EXISTING-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    Warehouse existingWarehouse = new Warehouse();
    existingWarehouse.businessUnitCode = "EXISTING-001";

    when(warehouseStore.findByBusinessUnitCode("EXISTING-001")).thenReturn(existingWarehouse);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForCreate(warehouse),
        "Should throw exception when business unit code exists");
  }

  @Test
  void testValidateForCreateFailsWhenLocationNotFound() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "INVALID-LOCATION";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));
    when(locationResolver.resolveByIdentifier("INVALID-LOCATION"))
        .thenThrow(new IllegalArgumentException("Location not found"));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForCreate(warehouse),
        "Should throw exception when location not found");
  }

  @Test
  void testValidateForCreateFailsWhenMaxWarehousesExceeded() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "ZWOLLE-001";
    warehouse.capacity = 30;
    warehouse.stock = 10;

    Location location = new Location("ZWOLLE-001", 1, 100);

    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "EXISTING-001";
    existing.location = "ZWOLLE-001";
    existing.capacity = 30;
    existing.archivedAt = null;

    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));
    when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(existing));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForCreate(warehouse),
        "Should throw exception when max warehouses exceeded");
  }

  @Test
  void testValidateForCreateFailsWhenStockExceedsCapacity() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 60; // Stock > Capacity

    Location location = new Location("AMSTERDAM-001", 5, 200);

    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));
    when(locationResolver.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForCreate(warehouse),
        "Should throw exception when stock exceeds capacity");
  }

  @Test
  void testValidateForCreateFailsWhenExceedsLocationCapacity() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "ZWOLLE-001";
    warehouse.capacity = 30;
    warehouse.stock = 20;

    Location location = new Location("ZWOLLE-001", 2, 40); // Max capacity 40

    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "EXISTING-001";
    existing.location = "ZWOLLE-001";
    existing.capacity = 35;
    existing.archivedAt = null;

    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));
    when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(existing));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForCreate(warehouse),
        "Should throw exception when exceeds location capacity");
  }

  // ==================== REPLACE VALIDATIONS ====================

  @Test
  void testValidateForReplaceSuccessfully() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 50;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 50;

    Location location = new Location("AMSTERDAM-001", 5, 300);

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    when(locationResolver.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(oldWarehouse));

    // When & Then - should not throw
    assertDoesNotThrow(() -> validator.validateForReplace(newWarehouse));
  }

  @Test
  void testValidateForReplaceFailsWhenWarehouseNotFound() {
    // Given
    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "NONEXISTENT";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 50;

    when(warehouseStore.findByBusinessUnitCode("NONEXISTENT"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForReplace(newWarehouse),
        "Should throw exception when warehouse not found");
  }

  @Test
  void testValidateForReplaceFailsWhenCapacityInsufficient() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 80;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 70; // Less than old stock - FAILS
    newWarehouse.stock = 80;

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForReplace(newWarehouse),
        "Should throw exception when capacity insufficient");
  }

  @Test
  void testValidateForReplaceFailsWhenStockMismatch() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 50;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 60; // Different from old - FAILS

    Location location = new Location("AMSTERDAM-001", 5, 300);

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    when(locationResolver.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(oldWarehouse));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForReplace(newWarehouse),
        "Should throw exception when stock doesn't match");
  }

  @Test
  void testValidateForReplaceFailsWhenLocationInvalid() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 50;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "INVALID-LOCATION";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 50;

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    when(locationResolver.resolveByIdentifier("INVALID-LOCATION"))
        .thenThrow(new IllegalArgumentException("Location not found"));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForReplace(newWarehouse),
        "Should throw exception when location invalid");
  }

  @Test
  void testValidateForReplaceFailsWhenNewStockExceedsCapacity() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 50;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 40;
    newWarehouse.stock = 50; // Stock > Capacity

    Location location = new Location("AMSTERDAM-001", 5, 300);

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    when(locationResolver.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForReplace(newWarehouse),
        "Should throw exception when stock exceeds capacity");
  }

  @Test
  void testValidateForReplaceWithLocationCapacityCheck() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "ZWOLLE-001";
    oldWarehouse.capacity = 30;
    oldWarehouse.stock = 20;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "ZWOLLE-001";
    newWarehouse.capacity = 20;
    newWarehouse.stock = 20;

    Location location = new Location("ZWOLLE-001", 2, 50);

    Warehouse otherWarehouse = new Warehouse();
    otherWarehouse.businessUnitCode = "MWH.002";
    otherWarehouse.location = "ZWOLLE-001";
    otherWarehouse.capacity = 20;
    otherWarehouse.archivedAt = null;

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(oldWarehouse, otherWarehouse));

    // When & Then - should not throw (20 + 20 = 40 <= 50 max capacity)
    assertDoesNotThrow(() -> validator.validateForReplace(newWarehouse));
  }

  @Test
  void testValidateForReplaceFailsWhenExceedsLocationCapacity() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "ZWOLLE-001";
    oldWarehouse.capacity = 30;
    oldWarehouse.stock = 20;
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "ZWOLLE-001";
    newWarehouse.capacity = 40;
    newWarehouse.stock = 20;

    Location location = new Location("ZWOLLE-001", 2, 50);

    Warehouse otherWarehouse = new Warehouse();
    otherWarehouse.businessUnitCode = "MWH.002";
    otherWarehouse.location = "ZWOLLE-001";
    otherWarehouse.capacity = 20;
    otherWarehouse.archivedAt = null;

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(oldWarehouse, otherWarehouse));

    // When & Then - should throw (30 + 20 = 50 currently, new warehouse would be 40 + 20 = 60 > 50)
    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateForReplace(newWarehouse),
        "Should throw exception when exceeds location capacity");
  }
}

