package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateWarehouseUseCaseTest {

  @Mock private WarehouseStore warehouseStore;
  @Mock private LocationGateway locationGateway;

  private CreateWarehouseUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new CreateWarehouseUseCase(warehouseStore);
    useCase.locationResolver = locationGateway;
  }

  @Test
  void testCreateWarehouseSuccessfully() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "NEW-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 50;
    warehouse.stock = 10;

    Location location = new Location("AMSTERDAM-001", 5, 100);

    when(locationGateway.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.getAll()).thenReturn(java.util.List.of());
    doNothing().when(warehouseStore).create(any());

    // When
    useCase.create(warehouse);

    // Then
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

    when(locationGateway.resolveByIdentifier("INVALID-LOCATION"))
        .thenThrow(new IllegalArgumentException("Location not found"));

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

    Location location = new Location("AMSTERDAM-001", 5, 100);

    Warehouse existingWarehouse = new Warehouse();
    existingWarehouse.businessUnitCode = "EXISTING-001";
    existingWarehouse.archivedAt = null;

    when(locationGateway.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.findByBusinessUnitCode("EXISTING-001"))
        .thenReturn(existingWarehouse);

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

    Location location = new Location("ZWOLLE-001", 1, 40);

    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "EXISTING-001";
    existing.location = "ZWOLLE-001";
    existing.capacity = 30;
    existing.archivedAt = null;

    when(locationGateway.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);
    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Not found"));
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(existing));

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

    Location location = new Location("AMSTERDAM-001", 5, 100);

    when(locationGateway.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Not found"));
    when(warehouseStore.getAll()).thenReturn(java.util.List.of());

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

    Location location = new Location("ZWOLLE-001", 2, 40); // Max capacity 40

    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "EXISTING-001";
    existing.location = "ZWOLLE-001";
    existing.capacity = 35;
    existing.archivedAt = null;

    when(locationGateway.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);
    when(warehouseStore.findByBusinessUnitCode("NEW-001"))
        .thenThrow(new IllegalArgumentException("Not found"));
    when(warehouseStore.getAll()).thenReturn(java.util.List.of(existing));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.create(warehouse),
        "Should throw exception when exceeds location max capacity");
  }
}
