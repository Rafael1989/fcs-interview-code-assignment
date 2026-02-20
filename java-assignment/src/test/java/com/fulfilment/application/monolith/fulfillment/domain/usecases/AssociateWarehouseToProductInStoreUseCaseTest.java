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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssociateWarehouseToProductInStoreUseCaseTest {

  @Mock private WarehouseRepository warehouseRepository;
  private TestProductRepository testProductRepository;
  private TestWarehouseProductStoreAssociationRepository testAssociationRepository;

  private AssociateWarehouseToProductInStoreUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new AssociateWarehouseToProductInStoreUseCase();
    testAssociationRepository = new TestWarehouseProductStoreAssociationRepository();
    useCase.setAssociationRepository(testAssociationRepository);
    useCase.setWarehouseRepository(warehouseRepository);
    testProductRepository = new TestProductRepository();
    useCase.setProductRepository(testProductRepository);
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
    testProductRepository.setTestProduct(null);

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

  private static class TestProductRepository extends ProductRepository {
    private Product testProduct;

    public void setTestProduct(Product product) {
      this.testProduct = product;
    }

    @Override
    public Product findById(Long id) {
      return testProduct;
    }
  }

  private static class TestWarehouseProductStoreAssociationRepository extends WarehouseProductStoreAssociationRepository {
    private WarehouseProductStoreAssociation createResult;
    private java.util.List<WarehouseProductStoreAssociation> findByStoreResult;
    private java.util.List<WarehouseProductStoreAssociation> findByWarehouseResult;
    private int countWarehousesForProductStoreResult;
    private int countWarehousesForStoreResult;
    private int countProductsForWarehouseResult;
    public void setCreateResult(WarehouseProductStoreAssociation result) { this.createResult = result; }
    public WarehouseProductStoreAssociation getCreateResult() { return createResult; }
    public void setFindByStoreResult(java.util.List<WarehouseProductStoreAssociation> result) { this.findByStoreResult = result; }
    public void setFindByWarehouseResult(java.util.List<WarehouseProductStoreAssociation> result) { this.findByWarehouseResult = result; }
    public void setCountWarehousesForProductStoreResult(int result) { this.countWarehousesForProductStoreResult = result; }
    public void setCountWarehousesForStoreResult(int result) { this.countWarehousesForStoreResult = result; }
    public void setCountProductsForWarehouseResult(int result) { this.countProductsForWarehouseResult = result; }
    @Override
    public WarehouseProductStoreAssociation create(WarehouseProductStoreAssociation association) {
        return createResult;
    }
    @Override
    public java.util.List<WarehouseProductStoreAssociation> findByStore(Long storeId) {
        return findByStoreResult;
    }
    @Override
    public java.util.List<WarehouseProductStoreAssociation> findByWarehouse(String warehouseCode) {
        return findByWarehouseResult;
    }
    @Override
    public int countWarehousesForProductStore(Long productId, Long storeId) {
        return countWarehousesForProductStoreResult;
    }
    @Override
    public int countWarehousesForStore(Long storeId) {
        return countWarehousesForStoreResult;
    }
    @Override
    public int countProductsForWarehouse(String warehouseCode) {
        return countProductsForWarehouseResult;
    }
  }
}
