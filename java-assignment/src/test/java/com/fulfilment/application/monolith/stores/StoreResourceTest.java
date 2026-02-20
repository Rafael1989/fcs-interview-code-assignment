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
import static org.mockito.Mockito.*;

@QuarkusTest
class StoreResourceTest {

    @Inject
    StoreResource resource;

    @BeforeEach
    @Transactional
    void setup() {
        Store.deleteAll();
        // Mock do gateway legado
        resource.legacyStoreManagerGateway = mock(LegacyStoreManagerGateway.class);
    }

    @Test
    @Transactional
    void testGet() {
        Store mockStore = new Store();
        mockStore.name = "Test";
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
        store.name = "NewStore";
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
        store.name = "StoreA";
        store.quantityProductsInStock = 5;
        store.persist();
        Store updated = new Store();
        updated.name = "StoreB";
        updated.quantityProductsInStock = 10;
        Store result = resource.update(store.id, updated);
        assertEquals("StoreB", result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testPatch() {
        Store store = new Store();
        store.name = "StoreA";
        store.quantityProductsInStock = 5;
        store.persist();
        Store updated = new Store();
        updated.name = "StoreB";
        updated.quantityProductsInStock = 10;
        Store result = resource.patch(store.id, updated);
        assertEquals("StoreB", result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testUpdateSuccess1() {
        Store store = new Store();
        store.name = "Update";
        store.quantityProductsInStock = 5;
        store.persist();

        Store updated = new Store();
        updated.name = "UpdatedName";
        updated.quantityProductsInStock = 10;

        Store result = resource.update(store.id, updated);
        assertEquals("UpdatedName", result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testGetSingleSuccess() {
        Store store = new Store();
        store.name = "GetSingle";
        store.quantityProductsInStock = 5;
        Store.persist(store);

        Store result = resource.getSingle(store.id);
        assertNotNull(result);
        assertEquals("GetSingle", result.name);
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
        store.name = "PatchStore";
        store.quantityProductsInStock = 10;
        Store.persist(store);

        Store patch = new Store();
        patch.name = "Patched";
        patch.quantityProductsInStock = 20;

        Store result = resource.patch(store.id, patch);
        assertEquals("Patched", result.name);
        assertEquals(20, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testPatchNoName() {
        Store store = new Store();
        store.name = "PatchNoName";
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
        store.name = "UpdateStore";
        store.quantityProductsInStock = 15;
        Store.persist(store);

        Store update = new Store();
        update.name = "Updated";
        update.quantityProductsInStock = 25;

        Store result = resource.update(store.id, update);
        assertEquals("Updated", result.name);
        assertEquals(25, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testDeleteSuccess() {
        Store store = new Store();
        store.name = "DeleteStore";
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
        store.name = "InvalidStock";
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
        store1.name = "StoreA";
        store1.quantityProductsInStock = 10;
        Store.persist(store1);

        Store store2 = new Store();
        store2.name = "StoreB";
        store2.quantityProductsInStock = 20;
        Store.persist(store2);

        List<Store> stores = resource.get();
        assertEquals(2, stores.size());
        assertTrue(stores.stream().anyMatch(s -> "StoreA".equals(s.name)));
        assertTrue(stores.stream().anyMatch(s -> "StoreB".equals(s.name)));
    }

    @Test
    @Transactional
    void testPatchPartialUpdate() {
        Store store = new Store();
        store.name = "PartialPatch";
        store.quantityProductsInStock = 5;
        Store.persist(store);

        Store patch = new Store();
        patch.name = "PartialPatched";
        // Não altera quantityProductsInStock

        Store result = resource.patch(store.id, patch);
        assertEquals("PartialPatched", result.name);
        assertEquals(5, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testUpdatePartial() {
        Store store = new Store();
        store.name = "PartialUpdate";
        store.quantityProductsInStock = 8;
        Store.persist(store);

        Store update = new Store();
        update.name = "PartialUpdated";

        Store result = resource.update(store.id, update);
        assertEquals("PartialUpdated", result.name);
        assertEquals(8, result.quantityProductsInStock);
    }
}
