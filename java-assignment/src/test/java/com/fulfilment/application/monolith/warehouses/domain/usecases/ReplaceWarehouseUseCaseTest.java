package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReplaceWarehouseUseCaseTest {

  @Mock private WarehouseStore warehouseStore;

  private ReplaceWarehouseUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new ReplaceWarehouseUseCase(warehouseStore);
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

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    doNothing().when(warehouseStore).update(any());
    doNothing().when(warehouseStore).create(any());

    // When
    useCase.replace(newWarehouse);

    // Then
    verify(warehouseStore).update(any()); // Old warehouse archived
    verify(warehouseStore).create(any()); // New warehouse created
  }

  @Test
  void testReplaceWarehouseFailsWhenCapacityInsufficient() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 80; // High stock

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 70; // Less than old stock - FAILS
    newWarehouse.stock = 80;

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.replace(newWarehouse),
        "Should throw exception when new capacity < old stock");
  }

  @Test
  void testReplaceWarehouseFailsWhenStockMismatch() {
    // Given
    Warehouse oldWarehouse = new Warehouse();
    oldWarehouse.businessUnitCode = "MWH.001";
    oldWarehouse.location = "AMSTERDAM-001";
    oldWarehouse.capacity = 100;
    oldWarehouse.stock = 50;

    Warehouse newWarehouse = new Warehouse();
    newWarehouse.businessUnitCode = "MWH.001";
    newWarehouse.location = "AMSTERDAM-001";
    newWarehouse.capacity = 150;
    newWarehouse.stock = 60; // Different from old: FAILS

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);

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

    when(warehouseStore.findByBusinessUnitCode("NONEXISTENT"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));

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

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);
    doNothing().when(warehouseStore).update(any());
    doNothing().when(warehouseStore).create(any());

    // When
    useCase.replace(newWarehouse);

    // Then
    verify(warehouseStore, times(1)).update(any());
    verify(warehouseStore, times(1)).create(any());
  }
}
