package com.fulfilment.application.monolith.products;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProductRepositoryTest {

    @Inject
    ProductRepository repository;

    @BeforeEach
    @Transactional
    void clean() {
        repository.deleteAll();
    }

    @Test
    @Transactional
    void testPersistAndFind() {
        Product product = new Product();
        product.name = "TestProduct";
        repository.getEntityManager().persist(product);
        assertNotNull(product.id);

        Product found = repository.findById(product.id);
        assertNotNull(found);
        assertEquals("TestProduct", found.name);
    }

    @Test
    @Transactional
    void testDelete() {
        Product product = new Product();
        product.name = "DeleteProduct";
        repository.getEntityManager().persist(product);
        assertNotNull(product.id);

        boolean deleted = repository.deleteById(product.id);
        assertTrue(deleted);
        Product found = repository.findById(product.id);
        assertNull(found);

        boolean deletedNonExistent = repository.deleteById(-1L);
        assertFalse(deletedNonExistent);
    }

    @Test
    @Transactional
    void testListAll() {
        repository.deleteAll();
        Product product1 = new Product();
        product1.name = "Product1";
        repository.getEntityManager().persist(product1);

        Product product2 = new Product();
        product2.name = "Product2";
        repository.getEntityManager().persist(product2);

        List<Product> all = repository.listAll();
        assertNotNull(all);
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(p -> "Product1".equals(p.name)));
        assertTrue(all.stream().anyMatch(p -> "Product2".equals(p.name)));
    }
}
