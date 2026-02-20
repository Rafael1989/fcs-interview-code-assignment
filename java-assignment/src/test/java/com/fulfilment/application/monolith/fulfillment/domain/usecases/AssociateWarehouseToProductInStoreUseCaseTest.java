package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseProductStoreAssociationRepository;
import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssociateWarehouseToProductInStoreUseCaseTest {

  @Mock private WarehouseProductStoreAssociationRepository associationRepository;
  @Mock private WarehouseRepository warehouseRepository;
  @Mock private ProductRepository productRepository;

  private AssociateWarehouseToProductInStoreUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new AssociateWarehouseToProductInStoreUseCase();
    useCase.associationRepository = associationRepository;
    useCase.warehouseRepository = warehouseRepository;
    useCase.productRepository = productRepository;
  }

  @Test
  void testAssociateSuccessfully() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "MWH.001";

    Product product = new Product();
    product.id = 1L;

    Store store = new Store();
    store.id = 1L;

    when(warehouseRepository.findByBusinessUnitCode("MWH.001")).thenReturn(warehouse);
    when(productRepository.findById(1L)).thenReturn(product);
    when(store.find("id", 1L)).thenReturn(store);
    when(associationRepository.countWarehousesForProductStore(1L, 1L)).thenReturn(0);
    when(associationRepository.countWarehousesForStore(1L)).thenReturn(1);
    when(associationRepository.findByStore(1L)).thenReturn(new ArrayList<>());
    when(associationRepository.countProductsForWarehouse("MWH.001")).thenReturn(2);
    when(associationRepository.findByWarehouse("MWH.001")).thenReturn(new ArrayList<>());

    WarehouseProductStoreAssociation created = new WarehouseProductStoreAssociation();
    created.id = 1L;
    created.warehouseBusinessUnitCode = "MWH.001";
    created.productId = 1L;
    created.storeId = 1L;

    when(associationRepository.create(any())).thenReturn(created);

    // When
    WarehouseProductStoreAssociation result = useCase.associate("MWH.001", 1L, 1L);

    // Then
    assertNotNull(result);
    assertEquals("MWH.001", result.warehouseBusinessUnitCode);
    assertEquals(1L, result.productId);
    assertEquals(1L, result.storeId);
  }

  @Test
  void testAssociateFailsWhenWarehouseNotFound() {
    // Given
    when(warehouseRepository.findByBusinessUnitCode("INVALID"))
        .thenThrow(new IllegalArgumentException("Warehouse not found"));

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("INVALID", 1L, 1L),
        "Should throw exception when warehouse not found");
  }

  @Test
  void testAssociateFailsWhenProductNotFound() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "MWH.001";

    when(warehouseRepository.findByBusinessUnitCode("MWH.001")).thenReturn(warehouse);
    when(productRepository.findById(999L)).thenReturn(null);

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("MWH.001", 999L, 1L),
        "Should throw exception when product not found");
  }

  @Test
  void testAssociateFailsWhenMaxProductsPerWarehouse() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "MWH.001";

    Product product = new Product();
    product.id = 1L;

    Store store = new Store();
    store.id = 1L;

    // Already has 5 products
    when(warehouseRepository.findByBusinessUnitCode("MWH.001")).thenReturn(warehouse);
    when(productRepository.findById(1L)).thenReturn(product);
    when(store.find("id", 1L)).thenReturn(store);
    when(associationRepository.countWarehousesForProductStore(1L, 1L)).thenReturn(0);
    when(associationRepository.countWarehousesForStore(1L)).thenReturn(2);
    when(associationRepository.findByStore(1L)).thenReturn(new ArrayList<>());
    when(associationRepository.countProductsForWarehouse("MWH.001")).thenReturn(5);

    List<WarehouseProductStoreAssociation> existingAssocs = new ArrayList<>();
    when(associationRepository.findByWarehouse("MWH.001")).thenReturn(existingAssocs);

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("MWH.001", 1L, 1L),
        "Should throw exception when max products for warehouse exceeded");
  }

  @Test
  void testAssociateFailsWhenMaxWarehousesPerProductStore() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "MWH.001";

    Product product = new Product();
    product.id = 1L;

    Store store = new Store();
    store.id = 1L;

    // Already has 2 warehouses for this product/store
    when(warehouseRepository.findByBusinessUnitCode("MWH.001")).thenReturn(warehouse);
    when(productRepository.findById(1L)).thenReturn(product);
    when(store.find("id", 1L)).thenReturn(store);
    when(associationRepository.countWarehousesForProductStore(1L, 1L)).thenReturn(2);

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("MWH.001", 1L, 1L),
        "Should throw exception when max warehouses for product/store exceeded");
  }

  @Test
  void testAssociateFailsWhenMaxWarehousesPerStore() {
    // Given
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "MWH.001";

    Product product = new Product();
    product.id = 1L;

    Store store = new Store();
    store.id = 1L;

    when(warehouseRepository.findByBusinessUnitCode("MWH.001")).thenReturn(warehouse);
    when(productRepository.findById(1L)).thenReturn(product);
    when(store.find("id", 1L)).thenReturn(store);
    when(associationRepository.countWarehousesForProductStore(1L, 1L)).thenReturn(1);
    when(associationRepository.countWarehousesForStore(1L)).thenReturn(3);

    List<WarehouseProductStoreAssociation> existingAssocs = new ArrayList<>();
    when(associationRepository.findByStore(1L)).thenReturn(existingAssocs);

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("MWH.001", 1L, 1L),
        "Should throw exception when max warehouses for store exceeded");
  }
}
