package com.fulfilment.application.monolith.warehouses.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.common.validation.RequestValidator;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.adapters.restapi.WarehouseErrorMapper;
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
  @Mock private RequestValidator validator;

  private WarehouseResourceImpl resource;

  @BeforeEach
  void setup() {
    resource = new WarehouseResourceImpl();
    resource.setWarehouseRepository(warehouseRepository);
    resource.setCreateWarehouseUseCase(createWarehouseUseCase);
    resource.setArchiveWarehouseUseCase(archiveWarehouseUseCase);
    resource.setReplaceWarehouseUseCase(replaceWarehouseUseCase);
    resource.setValidator(validator);
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
  void testCreateANewWarehouseUnitExceedsCapacity() {
    // Given
    com.warehouse.api.beans.Warehouse apiWarehouse = new com.warehouse.api.beans.Warehouse();
    apiWarehouse.setBusinessUnitCode("NEW-WH");
    apiWarehouse.setLocation("AMSTERDAM-001");
    apiWarehouse.setCapacity(200);
    apiWarehouse.setStock(50);

    doThrow(new IllegalArgumentException("Warehouse capacity would exceed maximum capacity for location: AMSTERDAM-001"))
        .when(createWarehouseUseCase).create(any());

    // When & Then
    jakarta.ws.rs.WebApplicationException ex =
        assertThrows(
            jakarta.ws.rs.WebApplicationException.class,
            () -> resource.createANewWarehouseUnit(apiWarehouse));
    assertEquals(422, ex.getResponse().getStatus());
  }

  @Test
  void testCreateANewWarehouseUnitDuplicateBusinessUnitCode() {
    // Given
    com.warehouse.api.beans.Warehouse apiWarehouse = new com.warehouse.api.beans.Warehouse();
    apiWarehouse.setBusinessUnitCode("EXISTING-WH");
    apiWarehouse.setLocation("AMSTERDAM-001");
    apiWarehouse.setCapacity(100);
    apiWarehouse.setStock(50);

    doThrow(new IllegalArgumentException("Business Unit Code already exists: EXISTING-WH"))
        .when(createWarehouseUseCase).create(any());

    // When & Then
    jakarta.ws.rs.WebApplicationException ex =
        assertThrows(
            jakarta.ws.rs.WebApplicationException.class,
            () -> resource.createANewWarehouseUnit(apiWarehouse));
    assertEquals(422, ex.getResponse().getStatus());
  }

  @Test
  void testGetAWarehouseUnitByID() {
    // Given
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.id = 1L;
    dbWarehouse.businessUnitCode = "WH-001";
    dbWarehouse.location = "AMSTERDAM-001";
    dbWarehouse.capacity = 100;
    dbWarehouse.stock = 50;

    when(validator.validateAndParseLongId("1", "Warehouse")).thenReturn(1L);
    when(warehouseRepository.findById(1L)).thenReturn(dbWarehouse);

    // When
    com.warehouse.api.beans.Warehouse result = resource.getAWarehouseUnitByID("1");

    // Then
    assertNotNull(result);
    assertEquals("WH-001", result.getBusinessUnitCode());
    assertEquals("AMSTERDAM-001", result.getLocation());
    assertEquals(100, result.getCapacity());
    assertEquals(50, result.getStock());
  }

  @Test
  void testGetAWarehouseUnitByIDNotFound() {
    // Given
    when(validator.validateAndParseLongId("999", "Warehouse")).thenReturn(999L);
    when(warehouseRepository.findById(999L)).thenReturn(null);

    // When & Then
    jakarta.ws.rs.WebApplicationException ex =
        assertThrows(
            jakarta.ws.rs.WebApplicationException.class,
            () -> resource.getAWarehouseUnitByID("999"));
    assertEquals(404, ex.getResponse().getStatus());
  }

  @Test
  void testArchiveAWarehouseUnitByID() {
    // Given
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.id = 1L;
    dbWarehouse.businessUnitCode = "WH-001";
    dbWarehouse.location = "AMSTERDAM-001";
    dbWarehouse.capacity = 100;
    dbWarehouse.stock = 50;

    when(validator.validateAndParseLongId("1", "Warehouse")).thenReturn(1L);
    when(warehouseRepository.findById(1L)).thenReturn(dbWarehouse);
    doNothing().when(archiveWarehouseUseCase).archive(any());

    // When
    resource.archiveAWarehouseUnitByID("1");

    // Then
    verify(warehouseRepository).findById(1L);
    verify(archiveWarehouseUseCase).archive(any(Warehouse.class));
  }

  @Test
  void testArchiveAWarehouseUnitByIDNotFound() {
    // Given
    when(validator.validateAndParseLongId("999", "Warehouse")).thenReturn(999L);
    when(warehouseRepository.findById(999L)).thenReturn(null);

    // When & Then
    jakarta.ws.rs.WebApplicationException ex =
        assertThrows(
            jakarta.ws.rs.WebApplicationException.class,
            () -> resource.archiveAWarehouseUnitByID("999"));
    assertEquals(404, ex.getResponse().getStatus());
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

  @Test
  void testErrorMapperWebApplicationException() {
    // Given
    WarehouseErrorMapper mapper = new WarehouseErrorMapper();
    mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    jakarta.ws.rs.WebApplicationException ex = new jakarta.ws.rs.WebApplicationException("error", jakarta.ws.rs.core.Response.status(422).build());

    // When
    jakarta.ws.rs.core.Response response = mapper.toResponse(ex);

    // Then
    assertEquals(422, response.getStatus());
    String entity = response.getEntity().toString();
    assertTrue(entity.contains("WebApplicationException"));
  }

  @Test
  void testErrorMapperGenericException() {
    // Given
    WarehouseErrorMapper mapper = new WarehouseErrorMapper();
    mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    Exception ex = new Exception("generic error");

    // When
    jakarta.ws.rs.core.Response response = mapper.toResponse(ex);

    // Then
    assertEquals(500, response.getStatus());
    String entity = response.getEntity().toString();
    assertTrue(entity.contains("Exception"));
    assertTrue(entity.contains("generic error"));
  }

  @Test
  void testErrorMapperIllegalArgumentException() {
    // Given
    WarehouseErrorMapper mapper = new WarehouseErrorMapper();
    mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    IllegalArgumentException ex = new IllegalArgumentException("Warehouse capacity would exceed maximum capacity for location: AMSTERDAM-001");

    // When
    jakarta.ws.rs.core.Response response = mapper.toResponse(ex);

    // Then
    assertEquals(422, response.getStatus());
    String entity = response.getEntity().toString();
    assertTrue(entity.contains("IllegalArgumentException"));
    assertTrue(entity.contains("Warehouse capacity would exceed maximum capacity"));
  }

  @Test
  void testErrorMapperConstraintViolationException() {
    // Given
    WarehouseErrorMapper mapper = new WarehouseErrorMapper();
    mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    jakarta.validation.ConstraintViolationException ex =
        new jakarta.validation.ConstraintViolationException("Validation failed", java.util.Set.of());

    // When
    jakarta.ws.rs.core.Response response = mapper.toResponse(ex);

    // Then
    assertEquals(400, response.getStatus());
    String entity = response.getEntity().toString();
    assertTrue(entity.contains("ConstraintViolationException"));
    assertTrue(entity.contains("Validation error"));
  }
}
