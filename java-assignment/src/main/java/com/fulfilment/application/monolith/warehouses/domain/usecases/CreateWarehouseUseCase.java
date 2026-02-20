package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;

  @Inject private LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void create(Warehouse warehouse) {
    // Validate location
    var location = locationResolver.resolveByIdentifier(warehouse.location);

    // Verify business unit code is unique
    try {
      warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
      throw new IllegalArgumentException(
          "Business Unit Code already exists: " + warehouse.businessUnitCode);
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("not found")) {
        // Good, code doesn't exist yet
      } else {
        throw e;
      }
    }

    // Check if can create warehouse at this location
    var warehousesAtLocation =
        warehouseStore.getAll().stream()
            .filter(w -> w.location.equals(warehouse.location) && w.archivedAt == null)
            .count();

    if (warehousesAtLocation >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException(
          "Maximum number of warehouses reached for location: " + warehouse.location);
    }

    // Validate capacity
    var totalCapacityAtLocation =
        warehouseStore.getAll().stream()
            .filter(w -> w.location.equals(warehouse.location) && w.archivedAt == null)
            .mapToInt(w -> w.capacity)
            .sum();

    if (totalCapacityAtLocation + warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException(
          "Warehouse capacity would exceed maximum capacity for location: " + warehouse.location);
    }

    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException(
          "Stock cannot exceed warehouse capacity");
    }

    warehouse.createdAt = LocalDateTime.now();

    // if all went well, create the warehouse
    warehouseStore.create(warehouse);
  }
}
