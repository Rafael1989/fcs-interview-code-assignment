package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    // Get the existing warehouse
    var existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

    // Validate capacity accommodation
    if (newWarehouse.capacity < existingWarehouse.stock) {
      throw new IllegalArgumentException(
          "New warehouse capacity cannot be less than existing warehouse stock");
    }

    // Validate stock matching
    if (!existingWarehouse.stock.equals(newWarehouse.stock)) {
      throw new IllegalArgumentException(
          "New warehouse stock must match existing warehouse stock");
    }

    // Archive the existing warehouse
    existingWarehouse.archivedAt = LocalDateTime.now();
    warehouseStore.update(existingWarehouse);

    // Create the new warehouse with the same business unit code
    newWarehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(newWarehouse);
  }
}
