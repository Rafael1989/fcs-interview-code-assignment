package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ArchiveWarehouseUseCaseTest {

  @Mock private WarehouseStore warehouseStore;

  private ArchiveWarehouseUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new ArchiveWarehouseUseCase(warehouseStore);
  }

  @Test
  void testArchiveWarehouseSuccessfully() {
    // Given
    Warehouse existingWarehouse = new Warehouse();
    existingWarehouse.businessUnitCode = "MWH.001";
    existingWarehouse.location = "AMSTERDAM-001";
    existingWarehouse.capacity = 100;
    existingWarehouse.stock = 50;
    existingWarehouse.createdAt = LocalDateTime.now().minusMonths(6);
    existingWarehouse.archivedAt = null;

    Warehouse warehouseToArchive = new Warehouse();
    warehouseToArchive.businessUnitCode = "MWH.001";

    when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(existingWarehouse);
    doNothing().when(warehouseStore).update(any());

    // When
    useCase.archive(warehouseToArchive);

    // Then
    ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);
    verify(warehouseStore).update(captor.capture());
    Warehouse archived = captor.getValue();
    assertNotNull(archived.archivedAt);
    assertEquals("MWH.001", archived.businessUnitCode);
  }

  @Test
  void testArchiveWarehouseFailsWhenNotFound() {
    // Given
    Warehouse warehouseToArchive = new Warehouse();
    warehouseToArchive.businessUnitCode = "NONEXISTENT";

    when(warehouseStore.findByBusinessUnitCode("NONEXISTENT"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> useCase.archive(warehouseToArchive),
        "Should throw exception when warehouse not found");
  }

  @Test
  void testArchiveAlreadyArchivedWarehouse() {
    // Given
    Warehouse alreadyArchivedWarehouse = new Warehouse();
    alreadyArchivedWarehouse.businessUnitCode = "MWH.002";
    alreadyArchivedWarehouse.location = "ZWOLLE-001";
    alreadyArchivedWarehouse.archivedAt = LocalDateTime.now().minusMonths(2);

    Warehouse warehouseToArchive = new Warehouse();
    warehouseToArchive.businessUnitCode = "MWH.002";

    when(warehouseStore.findByBusinessUnitCode("MWH.002")).thenReturn(alreadyArchivedWarehouse);
    doNothing().when(warehouseStore).update(any());

    // When
    useCase.archive(warehouseToArchive);

    // Then - Should still update (overwrite archived timestamp)
    ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);
    verify(warehouseStore).update(captor.capture());
    assertNotNull(captor.getValue().archivedAt);
  }
}
