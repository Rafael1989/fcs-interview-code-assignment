package com.fulfilment.application.monolith.fulfillment.domain.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class WarehouseProductStoreAssociationJunitTest {

    @Test
    void testDefaultValues() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assertNull(assoc.id);
        assertNull(assoc.warehouseBusinessUnitCode);
        assertNull(assoc.productId);
        assertNull(assoc.storeId);
        assertNull(assoc.createdAt);
    }

    @Test
    void testSetValues() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        assoc.id = 123L;
        assoc.warehouseBusinessUnitCode = "WBU-001";
        assoc.productId = 456L;
        assoc.storeId = 789L;
        LocalDateTime now = LocalDateTime.now();
        assoc.createdAt = now;
        assertEquals(123L, assoc.id);
        assertEquals("WBU-001", assoc.warehouseBusinessUnitCode);
        assertEquals(456L, assoc.productId);
        assertEquals(789L, assoc.storeId);
        assertEquals(now, assoc.createdAt);
    }

    @Test
    void testMultipleInstances() {
        WarehouseProductStoreAssociation assoc1 = new WarehouseProductStoreAssociation();
        WarehouseProductStoreAssociation assoc2 = new WarehouseProductStoreAssociation();
        assoc1.id = 1L;
        assoc2.id = 2L;
        assertNotEquals(assoc1.id, assoc2.id);
    }
}

