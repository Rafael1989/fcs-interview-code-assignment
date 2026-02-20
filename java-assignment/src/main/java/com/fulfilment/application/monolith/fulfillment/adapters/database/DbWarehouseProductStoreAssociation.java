package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_product_store_association")
@Cacheable
public class DbWarehouseProductStoreAssociation {

  @Id @GeneratedValue public Long id;

  public String warehouseBusinessUnitCode;

  public Long productId;

  public Long storeId;

  public LocalDateTime createdAt;

  public DbWarehouseProductStoreAssociation() {}

  public WarehouseProductStoreAssociation toAssociation() {
    var association = new WarehouseProductStoreAssociation();
    association.id = this.id;
    association.warehouseBusinessUnitCode = this.warehouseBusinessUnitCode;
    association.productId = this.productId;
    association.storeId = this.storeId;
    association.createdAt = this.createdAt;
    return association;
  }
}
