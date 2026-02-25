package com.fulfilment.application.monolith.warehouses.domain.validators;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Validator for Warehouse business logic rules.
 * This class centralizes all warehouse validation logic to ensure consistency
 * and maintainability across different warehouse use cases.
 */
@ApplicationScoped
public class WarehouseValidator {

  @Inject private WarehouseStore warehouseStore;

  @Inject private LocationResolver locationResolver;

  /**
   * Validates all constraints for creating a warehouse.
   * Includes: Business Unit Code uniqueness, Location validity, max warehouses per location,
   * and capacity/stock validation.
   *
   * @param warehouse the warehouse to validate
   * @throws IllegalArgumentException if any validation fails
   */
  public void validateForCreate(Warehouse warehouse) {
    validateBusinessUnitCodeDoesNotExist(warehouse.businessUnitCode);
    validateLocationExists(warehouse.location);
    validateMaxWarehousesNotExceeded(warehouse.location);
    validateCapacityAndStock(warehouse);
    validateCapacityAgainstLocationMaxCapacity(warehouse);
  }

  /**
   * Validates all constraints for replacing a warehouse.
   * Includes: All create validations + capacity accommodation and stock matching.
   *
   * @param newWarehouse the new warehouse to validate
   * @throws IllegalArgumentException if any validation fails
   */
  public void validateForReplace(Warehouse newWarehouse) {
    // Validate that the warehouse being replaced exists
    var existingWarehouse = validateWarehouseExists(newWarehouse.businessUnitCode);

    // Run all create validations for the new warehouse
    validateLocationExists(newWarehouse.location);
    validateCapacityAndStock(newWarehouse);
    validateCapacityAgainstLocationMaxCapacity(newWarehouse);

    // Additional validations specific to replacement
    validateCapacityAccommodatesExistingStock(newWarehouse, existingWarehouse);
    validateStockMatches(newWarehouse, existingWarehouse);
  }

  /**
   * Validates that a business unit code doesn't already exist.
   *
   * @param businessUnitCode the code to check
   * @throws IllegalArgumentException if the code already exists
   */
  private void validateBusinessUnitCodeDoesNotExist(String businessUnitCode) {
    try {
      warehouseStore.findByBusinessUnitCode(businessUnitCode);
      throw new IllegalArgumentException(
          "Business Unit Code already exists: " + businessUnitCode);
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().contains("not found")) {
        throw e;
      }
      // Warehouse not found is the expected case (code doesn't exist yet)
    }
  }

  /**
   * Validates that a warehouse with the given business unit code exists.
   *
   * @param businessUnitCode the code to check
   * @return the existing warehouse
   * @throws IllegalArgumentException if the warehouse doesn't exist
   */
  private Warehouse validateWarehouseExists(String businessUnitCode) {
    try {
      return warehouseStore.findByBusinessUnitCode(businessUnitCode);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Warehouse not found for replacement: " + businessUnitCode);
    }
  }

  /**
   * Validates that the specified location exists.
   *
   * @param locationIdentifier the location identifier
   * @throws IllegalArgumentException if the location doesn't exist
   */
  private void validateLocationExists(String locationIdentifier) {
    locationResolver.resolveByIdentifier(locationIdentifier);
    // If no exception is thrown, the location is valid
  }

  /**
   * Validates that the maximum number of warehouses hasn't been exceeded for a location.
   *
   * @param locationIdentifier the location identifier
   * @throws IllegalArgumentException if max warehouses would be exceeded
   */
  private void validateMaxWarehousesNotExceeded(String locationIdentifier) {
    var location = locationResolver.resolveByIdentifier(locationIdentifier);

    var warehousesAtLocation =
        warehouseStore.getAll().stream()
            .filter(w -> w.location.equals(locationIdentifier) && w.archivedAt == null)
            .count();

    if (warehousesAtLocation >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException(
          "Maximum number of warehouses reached for location: " + locationIdentifier);
    }
  }

  /**
   * Validates that warehouse stock does not exceed capacity.
   *
   * @param warehouse the warehouse to validate
   * @throws IllegalArgumentException if stock exceeds capacity
   */
  private void validateCapacityAndStock(Warehouse warehouse) {
    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException(
          "Stock cannot exceed warehouse capacity");
    }
  }

  /**
   * Validates that warehouse capacity doesn't exceed location's max capacity.
   *
   * @param newWarehouse the warehouse to validate
   * @throws IllegalArgumentException if capacity would exceed location's max capacity
   */
  private void validateCapacityAgainstLocationMaxCapacity(Warehouse newWarehouse) {
    var location = locationResolver.resolveByIdentifier(newWarehouse.location);

    var totalCapacityAtLocation =
        warehouseStore.getAll().stream()
            .filter(w -> w.location.equals(newWarehouse.location) && w.archivedAt == null)
            .mapToInt(w -> w.capacity)
            .sum();

    if (totalCapacityAtLocation + newWarehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException(
          "Warehouse capacity would exceed maximum capacity for location: " + newWarehouse.location);
    }
  }

  /**
   * Validates that the new warehouse's capacity can accommodate the existing warehouse's stock.
   *
   * @param newWarehouse the new warehouse
   * @param existingWarehouse the warehouse being replaced
   * @throws IllegalArgumentException if capacity is insufficient
   */
  private void validateCapacityAccommodatesExistingStock(
      Warehouse newWarehouse, Warehouse existingWarehouse) {
    if (newWarehouse.capacity < existingWarehouse.stock) {
      throw new IllegalArgumentException(
          "New warehouse capacity cannot be less than existing warehouse stock");
    }
  }

  /**
   * Validates that the new warehouse's stock matches the existing warehouse's stock.
   *
   * @param newWarehouse the new warehouse
   * @param existingWarehouse the warehouse being replaced
   * @throws IllegalArgumentException if stock doesn't match
   */
  private void validateStockMatches(Warehouse newWarehouse, Warehouse existingWarehouse) {
    if (!existingWarehouse.stock.equals(newWarehouse.stock)) {
      throw new IllegalArgumentException(
          "New warehouse stock must match existing warehouse stock");
    }
  }
}

