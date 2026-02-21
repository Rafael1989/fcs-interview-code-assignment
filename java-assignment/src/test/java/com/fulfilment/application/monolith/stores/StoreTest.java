package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Store entity to ensure proper code coverage
 * These tests verify Store constructors and toString method
 */
@QuarkusTest
class StoreTest {

    @Test
    void testStoreDefaultConstructor() {
        Store store = new Store();
        assertNotNull(store);
        assertNull(store.name);
        assertEquals(0, store.quantityProductsInStock);
    }

    @Test
    void testStoreConstructorWithName() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName);

        assertNotNull(store);
        assertEquals(storeName, store.name);
        assertEquals(0, store.quantityProductsInStock);
    }

    @Test
    void testStoreConstructorWithNameAndStock() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        int stock = 150;
        Store store = new Store(storeName, stock);

        assertNotNull(store);
        assertEquals(storeName, store.name);
        assertEquals(stock, store.quantityProductsInStock);
    }

    @Test
    void testStoreToString() {
        String storeName = "TestStore_" + System.currentTimeMillis();
        Store store = new Store(storeName, 100);

        String toString = store.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Store{"));
        assertTrue(toString.contains("name='" + storeName + "'"));
        assertTrue(toString.contains("quantityProductsInStock=100"));
    }

    @Test
    void testStoreToStringWithNullName() {
        Store store = new Store();
        String toString = store.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Store{"));
    }

    @Test
    void testStoreConstructorMultipleInstances() {
        Store store1 = new Store("Store1", 10);
        Store store2 = new Store("Store2", 20);
        Store store3 = new Store("Store3");

        assertEquals("Store1", store1.name);
        assertEquals(10, store1.quantityProductsInStock);
        assertEquals("Store2", store2.name);
        assertEquals(20, store2.quantityProductsInStock);
        assertEquals("Store3", store3.name);
        assertEquals(0, store3.quantityProductsInStock);
    }

    @Test
    void testStoreFieldAccess() {
        Store store = new Store("TestStore", 50);
        assertEquals("TestStore", store.name);
        assertEquals(50, store.quantityProductsInStock);

        // Test field modification
        store.name = "ModifiedStore";
        store.quantityProductsInStock = 75;
        assertEquals("ModifiedStore", store.name);
        assertEquals(75, store.quantityProductsInStock);
    }
}

