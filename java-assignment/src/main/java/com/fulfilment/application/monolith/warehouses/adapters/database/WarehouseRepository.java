package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    var dbWarehouse = new DbWarehouse();
    dbWarehouse.businessUnitCode = warehouse.businessUnitCode;
    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.createdAt = warehouse.createdAt;
    dbWarehouse.archivedAt = warehouse.archivedAt;
    dbWarehouse.persist();
  }

  @Override
  public void update(Warehouse warehouse) {
    var dbWarehouse = this.find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if (dbWarehouse == null) {
      throw new IllegalArgumentException("Warehouse not found: " + warehouse.businessUnitCode);
    }
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.archivedAt = warehouse.archivedAt;
    dbWarehouse.persist();
  }

  @Override
  public void remove(Warehouse warehouse) {
    var dbWarehouse = this.find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if (dbWarehouse == null) {
      throw new IllegalArgumentException("Warehouse not found: " + warehouse.businessUnitCode);
    }
    dbWarehouse.delete();
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    return this.find("businessUnitCode", buCode).firstResult()
        .map(DbWarehouse::toWarehouse)
        .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + buCode));
  }
}
