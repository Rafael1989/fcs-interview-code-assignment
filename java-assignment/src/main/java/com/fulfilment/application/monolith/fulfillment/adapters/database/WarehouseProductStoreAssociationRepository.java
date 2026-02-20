package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class WarehouseProductStoreAssociationRepository
    implements PanacheRepository<DbWarehouseProductStoreAssociation> {

  public WarehouseProductStoreAssociation create(WarehouseProductStoreAssociation association) {
    var dbAssociation = new DbWarehouseProductStoreAssociation();
    dbAssociation.warehouseBusinessUnitCode = association.warehouseBusinessUnitCode;
    dbAssociation.productId = association.productId;
    dbAssociation.storeId = association.storeId;
    dbAssociation.createdAt = LocalDateTime.now();
    dbAssociation.persist();
    return dbAssociation.toAssociation();
  }

  public List<WarehouseProductStoreAssociation> findByProductAndStore(Long productId, Long storeId) {
    return this.find("productId = ?1 and storeId = ?2", productId, storeId).list().stream()
        .map(DbWarehouseProductStoreAssociation::toAssociation)
        .toList();
  }

  public List<WarehouseProductStoreAssociation> findByStore(Long storeId) {
    return this.find("storeId", storeId).list().stream()
        .map(DbWarehouseProductStoreAssociation::toAssociation)
        .toList();
  }

  public List<WarehouseProductStoreAssociation> findByWarehouse(String warehouseCode) {
    return this.find("warehouseBusinessUnitCode", warehouseCode).list().stream()
        .map(DbWarehouseProductStoreAssociation::toAssociation)
        .toList();
  }

  public int countWarehousesForProductStore(Long productId, Long storeId) {
    return (int)
        this.find("productId = ?1 and storeId = ?2", productId, storeId).count();
  }

  public int countWarehousesForStore(Long storeId) {
    return (int) this.find("storeId", storeId).stream().map(a -> a.warehouseBusinessUnitCode)
        .distinct()
        .count();
  }

  public int countProductsForWarehouse(String warehouseCode) {
    return (int) this.find("warehouseBusinessUnitCode", warehouseCode).stream()
        .map(a -> a.productId)
        .distinct()
        .count();
  }
}
