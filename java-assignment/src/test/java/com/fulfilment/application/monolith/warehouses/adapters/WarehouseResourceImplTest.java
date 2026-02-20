package com.fulfilment.application.monolith.warehouses.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.adapters.restapi.WarehouseResourceImpl;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WarehouseResourceImplTest {

  @Mock private WarehouseRepository warehouseRepository;
  @Mock private CreateWarehouseUseCase createWarehouseUseCase;
  @Mock private ArchiveWarehouseUseCase archiveWarehouseUseCase;
  @Mock private ReplaceWarehouseUseCase replaceWarehouseUseCase;

  private WarehouseResourceImpl resource;

  @BeforeEach
  void setup() {
    resource = new WarehouseResourceImpl();
    resource.warehouseRepository = warehouseRepository;
    resource.createWarehouseUseCase = createWarehouseUseCase;
    resource.archiveWarehouseUseCase = archiveWarehouseUseCase;
    resource.replaceWarehouseUseCase = replaceWarehouseUseCase;
  }

  @Test
  void testListAllWarehousesUnitsEmpty() {
    // Given
    when(warehouseRepository.getAll()).thenReturn(new ArrayList<>());

    // When
    List<com.warehouse.api.beans.Warehouse> result = resource.listAllWarehousesUnits();

    // Then
    assertNotNull(result);
    assertEquals(0, result.size());
    verify(warehouseRepository).getAll();
  }

  @Test
  void testListAllWarehousesUnitsWithData() {
    // Given
    Warehouse w1 = new Warehouse();
    w1.businessUnitCode = "WH-001";
    w1.location = "AMSTERDAM-001";
    w1.capacity = 100;
    w1.stock = 50;

    Warehouse w2 = new Warehouse();
    w2.businessUnitCode = "WH-002";
    w2.location = "ZWOLLE-001";
    w2.capacity = 80;
    w2.stock = 30;

    when(warehouseRepository.getAll()).thenReturn(List.of(w1, w2));

    // When
    List<com.warehouse.api.beans.Warehouse> result = resource.listAllWarehousesUnits();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("WH-001", result.get(0).getBusinessUnitCode());
    assertEquals("WH-002", result.get(1).getBusinessUnitCode());
  }

  @Test
  void testCreateANewWarehouseUnit() {
    // Given
    com.warehouse.api.beans.Warehouse apiWarehouse = new com.warehouse.api.beans.Warehouse();
    apiWarehouse.setBusinessUnitCode("NEW-WH");
    apiWarehouse.setLocation("ROTTERDAM-001");
    apiWarehouse.setCapacity(120);
    apiWarehouse.setStock(40);

    doNothing().when(createWarehouseUseCase).create(any());

    // When
    com.warehouse.api.beans.Warehouse result = resource.createANewWarehouseUnit(apiWarehouse);

    // Then
    assertNotNull(result);
    assertEquals("NEW-WH", result.getBusinessUnitCode());
    verify(createWarehouseUseCase).create(any());
  }

  @Test
  void testGetAWarehouseUnitByID() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "WH-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 100;
    warehouse.stock = 50;

    when(warehouseRepository.findByBusinessUnitCode("WH-001")).thenReturn(warehouse);

    // When
    com.warehouse.api.beans.Warehouse result = resource.getAWarehouseUnitByID("WH-001");

    // Then
    assertNotNull(result);
    assertEquals("WH-001", result.getBusinessUnitCode());
    assertEquals("AMSTERDAM-001", result.getLocation());
    assertEquals(100, result.getCapacity());
    assertEquals(50, result.getStock());
  }

  @Test
  void testArchiveAWarehouseUnitByID() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "WH-001";
    warehouse.location = "AMSTERDAM-001";
    warehouse.capacity = 100;
    warehouse.stock = 50;

    when(warehouseRepository.findByBusinessUnitCode("WH-001")).thenReturn(warehouse);
    doNothing().when(archiveWarehouseUseCase).archive(any());

    // When
    resource.archiveAWarehouseUnitByID("WH-001");

    // Then
    verify(warehouseRepository).findByBusinessUnitCode("WH-001");
    verify(archiveWarehouseUseCase).archive(warehouse);
  }

  @Test
  void testReplaceTheCurrentActiveWarehouse() {
    // Given
    com.warehouse.api.beans.Warehouse apiWarehouse = new com.warehouse.api.beans.Warehouse();
    apiWarehouse.setBusinessUnitCode("WH-001");
    apiWarehouse.setLocation("ROTTERDAM-001");
    apiWarehouse.setCapacity(150);
    apiWarehouse.setStock(50);

    doNothing().when(replaceWarehouseUseCase).replace(any());

    // When
    com.warehouse.api.beans.Warehouse result = resource.replaceTheCurrentActiveWarehouse("WH-001", apiWarehouse);

    // Then
    assertNotNull(result);
    assertEquals("WH-001", result.getBusinessUnitCode());
    verify(replaceWarehouseUseCase).replace(any());
  }
}
