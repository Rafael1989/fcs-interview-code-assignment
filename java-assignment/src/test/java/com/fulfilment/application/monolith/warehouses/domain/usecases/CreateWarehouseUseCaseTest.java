package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validators.WarehouseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateWarehouseUseCaseTest {

  @Mock private WarehouseStore warehouseStore;
  @Mock private WarehouseValidator validator;

  private CreateWarehouseUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new CreateWarehouseUseCase(warehouseStore);
    useCase.setValidator(validator);
  }

  @Test
  void testCreateWarehouseSuccessfully() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    doNothing().when(validator).validateForCreate(any());
    doNothing().when(warehouseStore).create(any());

    // When
    useCase.create(warehouse);

    // Then
    verify(validator).validateForCreate(warehouse);
    verify(warehouseStore).create(warehouse);
    assertNotNull(warehouse.createdAt);
  }

  @Test
  void testCreateWarehouseFailsWhenLocationNotFound() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "INVALID-LOCATION";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    doThrow(new IllegalArgumentException("Location not found"))
        .when(validator).validateForCreate(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.create(warehouse),
        "Should throw exception for invalid location");
  }

  @Test
  void testCreateWarehouseFailsWhenDuplicateBusinessUnitCode() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "EXISTING-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    doThrow(new IllegalArgumentException(
        "Business Unit Code already exists: EXISTING-001"))
        .when(validator).validateForCreate(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.create(warehouse),
        "Should throw exception for duplicate business unit code");
  }

  @Test
  void testCreateWarehouseFailsWhenMaxWarehousesExceeded() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "ZWOLLE-001";
    warehouse.capacity = 30;
    warehouse.stock = 10;

    doThrow(new IllegalArgumentException(
        "Maximum number of warehouses reached for location: ZWOLLE-001"))
        .when(validator).validateForCreate(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.create(warehouse),
        "Should throw exception when max warehouses exceeded");
  }

  @Test
  void testCreateWarehouseFailsWhenStockExceedsCapacity() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 60; // Stock > Capacity

    doThrow(new IllegalArgumentException(
        "Stock cannot exceed warehouse capacity"))
        .when(validator).validateForCreate(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.create(warehouse),
        "Should throw exception when stock exceeds capacity");
  }

  @Test
  void testCreateWarehouseFailsWhenExceedsLocationCapacity() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "ZWOLLE-001";
    warehouse.capacity = 30;
    warehouse.stock = 20;

    doThrow(new IllegalArgumentException(
        "Warehouse capacity would exceed maximum capacity for location: ZWOLLE-001"))
        .when(validator).validateForCreate(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.create(warehouse),
        "Should throw exception when exceeds location max capacity");
  }
}
