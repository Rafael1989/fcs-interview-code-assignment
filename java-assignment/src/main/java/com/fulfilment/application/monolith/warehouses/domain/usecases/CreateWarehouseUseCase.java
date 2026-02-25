package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validators.WarehouseValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;

  @Inject private WarehouseValidator validator;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  public void setValidator(WarehouseValidator validator) {
    this.validator = validator;
  }

  @Override
  public void create(Warehouse warehouse) {
    // Validate all constraints for warehouse creation
    validator.validateForCreate(warehouse);

    // Set creation timestamp and persist
    warehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(warehouse);
  }
}
}
