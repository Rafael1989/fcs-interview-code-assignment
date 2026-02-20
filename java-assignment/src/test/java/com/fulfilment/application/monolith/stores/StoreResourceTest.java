package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class StoreResourceTest {

    @Inject
    StoreResource resource;

    @BeforeEach
    @Transactional
    void setup() {
        // Limpa o banco de dados em memória antes de cada teste
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
        Response response = resource.create(store);
        assertEquals(201, response.getStatus());
    }

    @Test
    void testCreateWithIdShouldFail() {
        Store store = new Store();
        store.id = 1L;
        assertThrows(WebApplicationException.class, () -> resource.create(store));
    }

    @Test
    void testUpdateNotFound() {
        Store store = new Store();
        store.name = "Update";
        assertThrows(WebApplicationException.class, () -> resource.update(999L, store));
    }

    @Test
    void testUpdateNoName() {
        Store store = new Store();
        assertThrows(WebApplicationException.class, () -> resource.update(1L, store));
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.delete(999L));
    }

    @Test
    @Transactional
    void testUpdateSuccess() {
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
    void testPatchSuccess() {
        Store store = new Store();
        store.name = "Patch";
        store.quantityProductsInStock = 3;
        store.persist();

        Store updated = new Store();
        updated.name = "PatchedName";
        updated.quantityProductsInStock = 7;

        Store result = resource.patch(store.id, updated);
        assertEquals("PatchedName", result.name);
        assertEquals(7, result.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testPatchNoName() {
        Store store = new Store();
        store.name = "Patch";
        store.quantityProductsInStock = 3;
        store.persist();

        Store updated = new Store();
        updated.quantityProductsInStock = 7;

        assertThrows(WebApplicationException.class, () -> resource.patch(store.id, updated));
    }

    @Test
    void testPatchNotFound() {
        Store updated = new Store();
        updated.name = "PatchedName";
        updated.quantityProductsInStock = 7;

        assertThrows(WebApplicationException.class, () -> resource.patch(999L, updated));
    }

    @Test
    @Transactional
    void testDeleteSuccess() {
        Store store = new Store();
        store.name = "Delete";
        store.quantityProductsInStock = 2;
        store.persist();

        Response response = resource.delete(store.id);
        assertEquals(204, response.getStatus());
    }
}
