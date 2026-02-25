package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validators.WarehouseValidator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReplaceWarehouseUseCaseTest {

  @Mock private WarehouseStore warehouseStore;
  @Mock private WarehouseValidator validator;

  private ReplaceWarehouseUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new ReplaceWarehouseUseCase(warehouseStore);
    useCase.setValidator(validator);
  }

  @Test
  void testReplaceWarehouseSuccessfully() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 50;
    oldWarehouse.createdAt = LocalDateTime.now().minusYears(3);
    oldWarehouse.archivedAt = null;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 50; // Same as old

    doNothing().when(validator).validateForReplace(any());
    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    doNothing().when(warehouseStore).update(any());
    doNothing().when(warehouseStore).create(any());

    // When
    useCase.replace(newWarehouse);

    // Then
    verify(validator).validateForReplace(any());
    verify(warehouseStore).update(any()); // Old warehouse archived
    verify(warehouseStore).create(any()); // New warehouse created
  }

  @Test
  void testReplaceWarehouseFailsWhenCapacityInsufficient() {
    // Given
    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 70; // Insufficient capacity
    newWarehouse.stock = 80;

    doThrow(new IllegalArgumentException(
        "New warehouse capacity cannot be less than existing warehouse stock"))
        .when(validator).validateForReplace(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.replace(newWarehouse),
        "Should throw exception when new capacity < old stock");
  }

  @Test
  void testReplaceWarehouseFailsWhenStockMismatch() {
    // Given
    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 60; // Different from old: FAILS

    doThrow(new IllegalArgumentException(
        "New warehouse stock must match existing warehouse stock"))
        .when(validator).validateForReplace(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.replace(newWarehouse),
        "Should throw exception when stock doesn't match");
  }

  @Test
  void testReplaceWarehouseFailsWhenNotFound() {
    // Given
    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "NONEXISTENT";

    doThrow(new IllegalArgumentException(
        "Warehouse not found for replacement: NONEXISTENT"))
        .when(validator).validateForReplace(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.replace(newWarehouse),
        "Should throw exception when old warehouse not found");
  }

  @Test
  void testReplaceWarehouseWithBetterCapacity() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 50;
    oldWarehouse.stock = 30;
    oldWarehouse.createdAt = LocalDateTime.now().minusYears(2);

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 200; // Much larger
    newWarehouse.stock = 30; // Same

    doNothing().when(validator).validateForReplace(any());
    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    doNothing().when(warehouseStore).update(any());
    doNothing().when(warehouseStore).create(any());

    // When
    useCase.replace(newWarehouse);

    // Then
    verify(validator).validateForReplace(any());
    verify(warehouseStore, times(1)).update(any());
    verify(warehouseStore, times(1)).create(any());
  }
}


