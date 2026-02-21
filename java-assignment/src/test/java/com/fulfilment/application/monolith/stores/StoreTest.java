package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Store entity to ensure proper code coverage
 * These tests directly exercise Store functionality
 */
@QuarkusTest
class StoreTest {

    @AfterEach
    @Transactional
    void cleanup() {
        Store.deleteAll();
    }

    @Test
    @Transactional
    void testStoreDefaultConstructor() {
        Store store = new Store();
        assertNotNull(store);
        assertNull(store.name);
        assertEquals(0, store.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testStoreConstructorWithName() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName);

        assertNotNull(store);
        assertEquals(storeName, store.name);
        assertEquals(0, store.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testStoreConstructorWithNameAndStock() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        int stock = 150;
        Store store = new Store(storeName, stock);

        assertNotNull(store);
        assertEquals(storeName, store.name);
        assertEquals(stock, store.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testStoreToString() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName, 100);
        store.persist();

        String toString = store.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Store{"));
        assertTrue(toString.contains("id="));
        assertTrue(toString.contains("name='" + storeName + "'"));
        assertTrue(toString.contains("quantityProductsInStock=100"));
    }

    @Test
    @Transactional
    void testStorePersistAndFind() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName, 50);
        store.persist();

        assertNotNull(store.id);

        Store found = Store.findById(store.id);
        assertNotNull(found);
        assertEquals(storeName, found.name);
        assertEquals(50, found.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testStoreUpdate() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName, 50);
        store.persist();

        store.quantityProductsInStock = 75;
        store.persist();

        Store found = Store.findById(store.id);
        assertEquals(75, found.quantityProductsInStock);
    }

    @Test
    @Transactional
    void testStoreDelete() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName, 50);
        store.persist();

        Long id = store.id;
        assertNotNull(Store.findById(id));

        store.delete();
        assertNull(Store.findById(id));
    }

    @Test
    @Transactional
    void testStoreFindByName() {
        String storeName = "UniqueStore_" + System.currentTimeMillis();
        Store store = new Store(storeName, 50);
        store.persist();

        Store found = Store.find("name", storeName).firstResult();
        assertNotNull(found);
        assertEquals(storeName, found.name);
    }

    @Test
    @Transactional
    void testStoreListAll() {
        Store store1 = new Store("StoreA_" + System.currentTimeMillis(), 10);
        Store store2 = new Store("StoreB_" + System.currentTimeMillis(), 20);
        store1.persist();
        store2.persist();

        var stores = Store.listAll();
        assertTrue(stores.size() >= 2);
    }

    @Test
    @Transactional
    void testStoreCount() {
        long initialCount = Store.count();

        Store store1 = new Store("StoreCount1_" + System.currentTimeMillis(), 10);
        Store store2 = new Store("StoreCount2_" + System.currentTimeMillis(), 20);
        store1.persist();
        store2.persist();

        assertEquals(initialCount + 2, Store.count());
    }
}

