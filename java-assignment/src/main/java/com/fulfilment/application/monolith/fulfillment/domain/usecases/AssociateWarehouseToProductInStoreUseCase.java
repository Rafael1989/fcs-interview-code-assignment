package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseProductStoreAssociationRepository;
import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class AssociateWarehouseToProductInStoreUseCase {

  @Inject private WarehouseProductStoreAssociationRepository associationRepository;
  @Inject private WarehouseRepository warehouseRepository;
  @Inject private ProductRepository productRepository;

  public WarehouseProductStoreAssociation associate(
      String warehouseBusinessUnitCode, Long productId, Long storeId) {

    // Validate warehouse exists
    var warehouse = warehouseRepository.findByBusinessUnitCode(warehouseBusinessUnitCode);
    if (warehouse == null) {
      throw new WebApplicationException("Warehouse not found: " + warehouseBusinessUnitCode, 404);
    }

    // Validate product exists
    var product = productRepository.findById(productId);
    if (product == null) {
      throw new WebApplicationException("Product not found: " + productId, 404);
    }

    // Validate store exists
    var store = Store.findById(storeId);
    if (store == null) {
      throw new WebApplicationException("Store not found: " + storeId, 404);
    }

    // Validate constraint 1: Each Product can be fulfilled by max 2 different Warehouses per Store
    int warehousesForProductStore =
        associationRepository.countWarehousesForProductStore(productId, storeId);
    if (warehousesForProductStore >= 2) {
      throw new WebApplicationException(
          "Maximum number of warehouses (2) reached for this product in this store", 400);
    }

    // Validate constraint 2: Each Store can be fulfilled by max 3 different Warehouses
    int warehousesForStore = associationRepository.countWarehousesForStore(storeId);
    if (warehousesForStore >= 3
        && !associationRepository.findByStore(storeId).stream()
            .anyMatch(a -> a.warehouseBusinessUnitCode.equals(warehouseBusinessUnitCode))) {
      throw new WebApplicationException(
          "Maximum number of warehouses (3) reached for this store", 400);
    }

    // Validate constraint 3: Each Warehouse can store max 5 types of Products
    int productsForWarehouse =
        associationRepository.countProductsForWarehouse(warehouseBusinessUnitCode);
    if (productsForWarehouse >= 5
        && !associationRepository.findByWarehouse(warehouseBusinessUnitCode).stream()
            .anyMatch(a -> a.productId.equals(productId))) {
      throw new WebApplicationException(
          "Maximum number of products (5) reached for this warehouse", 400);
    }

    // Create the association
    var association = new WarehouseProductStoreAssociation();
    association.warehouseBusinessUnitCode = warehouseBusinessUnitCode;
    association.productId = productId;
    association.storeId = storeId;

    return associationRepository.create(association);
  }
}
