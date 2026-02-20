package com.fulfilment.application.monolith.fulfillment.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseProductStoreAssociationRepository;
import com.fulfilment.application.monolith.fulfillment.adapters.restapi.FulfillmentResourceImpl;
import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import com.fulfilment.application.monolith.fulfillment.domain.usecases.AssociateWarehouseToProductInStoreUseCase;
import jakarta.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FulfillmentResourceImplTest {

  @Mock private AssociateWarehouseToProductInStoreUseCase associateUseCase;
  @Mock private WarehouseProductStoreAssociationRepository associationRepository;

  private FulfillmentResourceImpl resource;

  @BeforeEach
  void setup() throws Exception {
    resource = new FulfillmentResourceImpl();
    // Use reflection to set private fields
    setPrivateField(resource, "associateUseCase", associateUseCase);
    setPrivateField(resource, "associationRepository", associationRepository);
  }

  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @Test
  void testAssociateSuccessfully() {
    // Given
    WarehouseProductStoreAssociation association = new WarehouseProductStoreAssociation();
    association.id = 1L;
    association.warehouseBusinessUnitCode = "WH-001";
    association.productId = 1L;
    association.storeId = 1L;
    association.createdAt = LocalDateTime.now();

    when(associateUseCase.associate("WH-001", 1L, 1L)).thenReturn(association);

    // When
    Response response = resource.associate("WH-001", 1L, 1L);

    // Then
    assertEquals(201, response.getStatus());
    assertNotNull(response.getEntity());
    verify(associateUseCase).associate("WH-001", 1L, 1L);
  }

  @Test
  void testGetAssociationsByWarehouseEmpty() {
    // Given
    when(associationRepository.findByWarehouse("WH-001")).thenReturn(new ArrayList<>());

    // When
    List<Map<String, Object>> result = resource.getAssociationsByWarehouse("WH-001");

    // Then
    assertNotNull(result);
    assertEquals(0, result.size());
    verify(associationRepository).findByWarehouse("WH-001");
  }

  @Test
  void testGetAssociationsByWarehouse() {
    // Given
    WarehouseProductStoreAssociation a1 = new WarehouseProductStoreAssociation();
    a1.id = 1L;
    a1.warehouseBusinessUnitCode = "WH-001";
    a1.productId = 1L;
    a1.storeId = 1L;
    a1.createdAt = LocalDateTime.now();

    WarehouseProductStoreAssociation a2 = new WarehouseProductStoreAssociation();
    a2.id = 2L;
    a2.warehouseBusinessUnitCode = "WH-001";
    a2.productId = 2L;
    a2.storeId = 1L;
    a2.createdAt = LocalDateTime.now();

    when(associationRepository.findByWarehouse("WH-001")).thenReturn(List.of(a1, a2));

    // When
    List<Map<String, Object>> result = resource.getAssociationsByWarehouse("WH-001");

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(1L, result.get(0).get("id"));
    assertEquals(2L, result.get(1).get("id"));
  }

  @Test
  void testGetAssociationsByStoreEmpty() {
    // Given
    when(associationRepository.findByStore(1L)).thenReturn(new ArrayList<>());

    // When
    List<Map<String, Object>> result = resource.getAssociationsByStore(1L);

    // Then
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void testGetAssociationsByStore() {
    // Given
    WarehouseProductStoreAssociation a1 = new WarehouseProductStoreAssociation();
    a1.id = 1L;
    a1.warehouseBusinessUnitCode = "WH-001";
    a1.productId = 1L;
    a1.storeId = 1L;
    a1.createdAt = LocalDateTime.now();

    when(associationRepository.findByStore(1L)).thenReturn(List.of(a1));

    // When
    List<Map<String, Object>> result = resource.getAssociationsByStore(1L);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("WH-001", result.get(0).get("warehouseBusinessUnitCode"));
  }

  @Test
  void testGetAssociationsByProductStoreEmpty() {
    // Given
    when(associationRepository.findByProductAndStore(1L, 1L)).thenReturn(new ArrayList<>());

    // When
    List<Map<String, Object>> result = resource.getAssociationsByProductStore(1L, 1L);

    // Then
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void testGetAssociationsByProductStore() {
    // Given
    WarehouseProductStoreAssociation a1 = new WarehouseProductStoreAssociation();
    a1.id = 1L;
    a1.warehouseBusinessUnitCode = "WH-001";
    a1.productId = 1L;
    a1.storeId = 1L;
    a1.createdAt = LocalDateTime.now();

    when(associationRepository.findByProductAndStore(1L, 1L)).thenReturn(List.of(a1));

    // When
    List<Map<String, Object>> result = resource.getAssociationsByProductStore(1L, 1L);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).get("productId"));
    assertEquals(1L, result.get(0).get("storeId"));
  }
}
