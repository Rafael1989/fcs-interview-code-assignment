package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class StoreResourceTest {

    @Inject
    StoreResource resource;

    @BeforeEach
    @Transactional
    void setup() {
        // Clear all stores before each test
        Store.deleteAll();
        // NOTE: NOT mocking legacyStoreManagerGateway to ensure real code execution for coverage
    }

    @Test
    @Transactional
    void testGet() {
        Store mockStore = new Store();
        mockStore.name = "Test_" + System.currentTimeMillis();
        mockStore.quantityProductsInStock = 1;
        mockStore.persist();
        assertFalse(resource.get().isEmpty());
    }

    @Test
    void testGetSingleNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.getSingle(999L));
    }

    @Test
    @Transactional
    void testCreate() {
        Store store = new Store();
        store.name = "NewStore_" + System.currentTimeMillis();
        store.quantityProductsInStock = 1;
        store.id = null;
        try (Response response = resource.create(store)) {
            assertEquals(201, response.getStatus());
        }
    }

    @Test
    void testCreateWithIdShouldFail() {
        Store store = new Store();
        store.id = 1L;
        assertThrows(WebApplicationException.class, () -> resource.create(store));
    }

    @Test
    @Transactional
    void testDeleteNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.delete(999L));
    }

    @Test
    @Transactional
    void testUpdate() {
        Store store = new Store();
        store.name = "StoreA_" + System.currentTimeMillis();
        store.quantityProductsInStock = 5;
        store.persist();
        Store updated = new Store();
        updated.name = "StoreB_" + System.currentTimeMillis();
        updated.quantityProductsInStock = 10;
        Store result = resource.update(store.id, updated);
        assertEquals(updated.name, result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testPatch() {
        Store store = new Store();
        store.name = "StoreA_Patch_" + System.currentTimeMillis();
        store.quantityProductsInStock = 5;
        store.persist();
        Store updated = new Store();
        updated.name = "StoreB_Patch_" + System.currentTimeMillis();
        updated.quantityProductsInStock = 10;
        Store result = resource.patch(store.id, updated);
        assertEquals(updated.name, result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testUpdateSuccess1() {
        Store store = new Store();
        store.name = "Update_" + System.currentTimeMillis();
        store.quantityProductsInStock = 5;
        store.persist();

        Store updated = new Store();
        updated.name = "UpdatedName_" + System.currentTimeMillis();
        updated.quantityProductsInStock = 10;

        Store result = resource.update(store.id, updated);
        assertEquals(updated.name, result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testGetSingleSuccess() {
        Store store = new Store();
        store.name = "GetSingle_" + System.currentTimeMillis();
        store.quantityProductsInStock = 5;
        Store.persist(store);

        Store result = resource.getSingle(store.id);
        assertNotNull(result);
        assertEquals(store.name, result.name);
    }

    @Test
    @Transactional
    void testGetAllEmpty() {
        Store.deleteAll();
        List<Store> stores = resource.get();
        assertTrue(stores.isEmpty());
    }

    @Test
    @Transactional
    void testPatchSuccess() {
        Store store = new Store();
        store.name = "PatchStore_" + System.currentTimeMillis();
        store.quantityProductsInStock = 10;
        Store.persist(store);

        Store patch = new Store();
        patch.name = "Patched_" + System.currentTimeMillis();
        patch.quantityProductsInStock = 20;

        Store result = resource.patch(store.id, patch);
        assertEquals(patch.name, result.name);
        assertEquals(20, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testPatchNoName() {
        Store store = new Store();
        store.name = "PatchNoName_" + System.currentTimeMillis();
        store.quantityProductsInStock = 10;
        Store.persist(store);

        Store patch = new Store();
        patch.quantityProductsInStock = 20;

        assertThrows(WebApplicationException.class, () -> resource.patch(store.id, patch));
    }

    @Test
    @Transactional
    void testPatchNotFound() {
        Store patch = new Store();
        patch.name = "Patched";
        patch.quantityProductsInStock = 20;

        assertThrows(WebApplicationException.class, () -> resource.patch(99999L, patch));
    }

    @Test
    @Transactional
    void testUpdateSuccess2() {
        Store store = new Store();
        store.name = "UpdateStore_" + System.currentTimeMillis();
        store.quantityProductsInStock = 15;
        Store.persist(store);

        Store update = new Store();
        update.name = "Updated_" + System.currentTimeMillis();
        update.quantityProductsInStock = 25;

        Store result = resource.update(store.id, update);
        assertEquals(update.name, result.name);
        assertEquals(25, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testDeleteSuccess() {
        Store store = new Store();
        store.name = "DeleteStore_" + System.currentTimeMillis();
        store.quantityProductsInStock = 5;
        Store.persist(store);

        Response response = resource.delete(store.id);
        assertEquals(204, response.getStatus());
    }

    @Test
    @Transactional
    void testCreateNullNameShouldFail() {
        Store store = new Store();
        store.quantityProductsInStock = 1;
        store.id = null;
        assertThrows(WebApplicationException.class, () -> {
            try (Response ignored = resource.create(store)) {}
        });
    }

    @Test
    @Transactional
    void testCreateNegativeStockShouldFail() {
        Store store = new Store();
        store.name = "InvalidStock_" + System.currentTimeMillis();
        store.quantityProductsInStock = -10;
        store.id = null;
        assertThrows(WebApplicationException.class, () -> {
            try (Response ignored = resource.create(store)) {}
        });
    }

    @Test
    @Transactional
    void testMultipleStoreCreationAndGet() {
        Store store1 = new Store();
        store1.name = "StoreA_" + System.currentTimeMillis();
        store1.quantityProductsInStock = 10;
        Store.persist(store1);

        Store store2 = new Store();
        store2.name = "StoreB_" + System.currentTimeMillis();
        store2.quantityProductsInStock = 20;
        Store.persist(store2);

        List<Store> stores = resource.get();
        assertEquals(2, stores.size());
        assertTrue(stores.stream().anyMatch(s -> s.name.startsWith("StoreA_")));
        assertTrue(stores.stream().anyMatch(s -> s.name.startsWith("StoreB_")));
    }

    @Test
    @Transactional
    void testPatchPartialUpdate() {
        Store store = new Store();
        store.name = "PartialPatch_" + System.currentTimeMillis();
        store.quantityProductsInStock = 5;
        Store.persist(store);

        Store patch = new Store();
        patch.name = "PartialPatched_" + System.currentTimeMillis();
        // Does not alter quantityProductsInStock

        Store result = resource.patch(store.id, patch);
        assertEquals(patch.name, result.name);
        assertEquals(5, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testUpdatePartial() {
        Store store = new Store();
        store.name = "PartialUpdate_" + System.currentTimeMillis();
        store.quantityProductsInStock = 8;
        Store.persist(store);

        Store update = new Store();
        update.name = "PartialUpdated_" + System.currentTimeMillis();

        Store result = resource.update(store.id, update);
        assertEquals(update.name, result.name);
        assertEquals(8, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testUpdateWithDuplicateNameShouldFail() {
        Store store1 = new Store();
        store1.name = "ExistingStore_" + System.currentTimeMillis();
        store1.quantityProductsInStock = 10;
        Store.persist(store1);

        Store store2 = new Store();
        store2.name = "AnotherStore_" + System.currentTimeMillis();
        store2.quantityProductsInStock = 5;
        Store.persist(store2);

        Store updateWithDuplicate = new Store();
        updateWithDuplicate.name = store1.name; // Try to use the same name as store1

        assertThrows(WebApplicationException.class, () -> resource.update(store2.id, updateWithDuplicate));
    }

    @Test
    @Transactional
    void testPatchWithDuplicateNameShouldFail() {
        Store store1 = new Store();
        store1.name = "ExistingStore2_" + System.currentTimeMillis();
        store1.quantityProductsInStock = 10;
        Store.persist(store1);

        Store store2 = new Store();
        store2.name = "AnotherStore2_" + System.currentTimeMillis();
        store2.quantityProductsInStock = 5;
        Store.persist(store2);

        Store patchWithDuplicate = new Store();
        patchWithDuplicate.name = store1.name; // Try to use the same name as store1

        assertThrows(WebApplicationException.class, () -> resource.patch(store2.id, patchWithDuplicate));
    }

    @Test
    @Transactional
    void testUpdateWithSameNameShouldSucceed() {
        Store store = new Store();
        store.name = "SameName_" + System.currentTimeMillis();
        store.quantityProductsInStock = 10;
        Store.persist(store);

        Store update = new Store();
        update.name = store.name; // Use the same name
        update.quantityProductsInStock = 20;

        Store result = resource.update(store.id, update);
        assertEquals(store.name, result.name);
        assertEquals(20, result.quantityProductsInStock);
    }
}
