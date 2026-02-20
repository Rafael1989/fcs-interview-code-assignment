package com.fulfilment.application.monolith.fulfillment.domain.models;

import java.time.LocalDateTime;

public class WarehouseProductStoreAssociation {

  public Long id;

  public String warehouseBusinessUnitCode;

  public Long productId;

  public Long storeId;

  public LocalDateTime createdAt;

  public WarehouseProductStoreAssociation() {}

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getWarehouseBusinessUnitCode() { return warehouseBusinessUnitCode; }
  public void setWarehouseBusinessUnitCode(String warehouseBusinessUnitCode) { this.warehouseBusinessUnitCode = warehouseBusinessUnitCode; }

  public Long getProductId() { return productId; }
  public void setProductId(Long productId) { this.productId = productId; }

  public Long getStoreId() { return storeId; }
  public void setStoreId(Long storeId) { this.storeId = storeId; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
