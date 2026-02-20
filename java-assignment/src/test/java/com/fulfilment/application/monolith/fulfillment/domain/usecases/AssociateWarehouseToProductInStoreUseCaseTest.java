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
    useCase.setAssociationRepository(associationRepository);
    useCase.setWarehouseRepository(warehouseRepository);
    useCase.setProductRepository(productRepository);
  }

  @Test
  void testAssociateFailsWhenWarehouseNotFound() {
    // Given
    when(warehouseRepository.findByBusinessUnitCode("INVALID")).thenReturn(null);

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("INVALID", 1L, 1L),
        "Should throw exception when warehouse not found");
  }

  @Test
  void testAssociateFailsWhenProductNotFound() {
    // Given
    when(warehouseRepository.findByBusinessUnitCode("MWH.001"))
        .thenReturn(createWarehouse("MWH.001"));
    when(productRepository.findById(999L)).thenReturn(null);

    // When & Then
    assertThrows(
        WebApplicationException.class,
        () -> useCase.associate("MWH.001", 999L, 1L),
        "Should throw exception when product not found");
  }

  // Helper methods

  private Warehouse createWarehouse(String businessUnitCode) {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = businessUnitCode;
    return warehouse;
  }

  private Product createProduct(Long id) {
    Product product = new Product();
    product.id = id;
    return product;
  }

  private Store createStore(Long id) {
    Store store = new Store();
    store.id = id;
    return store;
  }
}
