package com.fulfilment.application.monolith.fulfillment.domain.models;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class WarehouseProductStoreAssociation {

  public Long id;

  public String warehouseBusinessUnitCode;

  public Long productId;

  public Long storeId;

  public LocalDateTime createdAt;

  // Construtor padrão para cobertura Jacoco
  public WarehouseProductStoreAssociation() {}
}
