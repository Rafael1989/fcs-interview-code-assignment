package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class WarehouseProductStoreAssociationRepositoryTest {

    @Inject
    WarehouseProductStoreAssociationRepository repo;

    @BeforeEach
    @Transactional
    void clean() {
        repo.deleteAll();
    }

    @Test
    @Transactional
    void testCreateAndFind() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W1";
        assoc.productId = 1L;
        assoc.storeId = 2L;
        WarehouseProductStoreAssociation created = repo.create(assoc);
        assertNotNull(created);
        List<WarehouseProductStoreAssociation> found = repo.findByProductAndStore(1L, 2L);
        assertFalse(found.isEmpty());
    }

    @Test
    @Transactional
    void testFindByProductAndStore() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W2";
        assoc.productId = 10L;
        assoc.storeId = 20L;
        repo.create(assoc);
        List<WarehouseProductStoreAssociation> list = repo.findByProductAndStore(10L, 20L);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Transactional
    void testFindByStore() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W3";
        assoc.productId = 100L;
        assoc.storeId = 200L;
        repo.create(assoc);
        List<WarehouseProductStoreAssociation> list = repo.findByStore(200L);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Transactional
    void testFindByWarehouse() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W4";
        assoc.productId = 1000L;
        assoc.storeId = 2000L;
        repo.create(assoc);
        List<WarehouseProductStoreAssociation> list = repo.findByWarehouse("W4");
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Transactional
    void testCountWarehousesForProductStore() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W5";
        assoc.productId = 10000L;
        assoc.storeId = 20000L;
        repo.create(assoc);
        int count = repo.countWarehousesForProductStore(10000L, 20000L);
        assertTrue(count >= 1);
    }

    @Test
    @Transactional
    void testCountWarehousesForStore() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W6";
        assoc.productId = 100000L;
        assoc.storeId = 200000L;
        repo.create(assoc);
        int count = repo.countWarehousesForStore(200000L);
        assertTrue(count >= 1);
    }

    @Test
    @Transactional
    void testCountProductsForWarehouse() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.warehouseBusinessUnitCode = "W7";
        assoc.productId = 1000000L;
        assoc.storeId = 2000000L;
        repo.create(assoc);
        int count = repo.countProductsForWarehouse("W7");
        assertTrue(count >= 1);
    }
}
