package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;
  @Inject private CreateWarehouseUseCase createWarehouseUseCase;
  @Inject private ArchiveWarehouseUseCase archiveWarehouseUseCase;
  @Inject private ReplaceWarehouseUseCase replaceWarehouseUseCase;

  // Public constructor for JaCoCo coverage
  public WarehouseResourceImpl() {}

  // Setters for testing
  public void setWarehouseRepository(WarehouseRepository warehouseRepository) {
    this.warehouseRepository = warehouseRepository;
  }

  public void setCreateWarehouseUseCase(CreateWarehouseUseCase createWarehouseUseCase) {
    this.createWarehouseUseCase = createWarehouseUseCase;
  }

  public void setArchiveWarehouseUseCase(ArchiveWarehouseUseCase archiveWarehouseUseCase) {
    this.archiveWarehouseUseCase = archiveWarehouseUseCase;
  }

  public void setReplaceWarehouseUseCase(ReplaceWarehouseUseCase replaceWarehouseUseCase) {
    this.replaceWarehouseUseCase = replaceWarehouseUseCase;
  }

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    var warehouse = toDomainWarehouse(data);
    createWarehouseUseCase.create(warehouse);
    return data;
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    var warehouse = warehouseRepository.findByBusinessUnitCode(id);
    return toWarehouseResponse(warehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    var warehouse = warehouseRepository.findByBusinessUnitCode(id);
    archiveWarehouseUseCase.archive(warehouse);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull Warehouse data) {
    var newWarehouse = toDomainWarehouse(data);
    newWarehouse.businessUnitCode = businessUnitCode;
    replaceWarehouseUseCase.replace(newWarehouse);
    return data;
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomainWarehouse(
      Warehouse warehouse) {
    var domain = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
    domain.businessUnitCode = warehouse.getBusinessUnitCode();
    domain.location = warehouse.getLocation();
    domain.capacity = warehouse.getCapacity();
    domain.stock = warehouse.getStock();
    return domain;
  }
}
