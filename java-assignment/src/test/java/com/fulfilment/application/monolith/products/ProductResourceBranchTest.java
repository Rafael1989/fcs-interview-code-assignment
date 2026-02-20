package com.fulfilment.application.monolith.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductResourceBranchTest {

    @Test
    void testGetSingleNotFound() {
        ProductResource resource = new ProductResource();
        ProductRepository repo = mock(ProductRepository.class);
        resource.productRepository = repo;
        when(repo.findById(1L)).thenReturn(null);
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.getSingle(1L));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void testCreateWithIdShouldFail() {
        ProductResource resource = new ProductResource();
        ProductRepository repo = mock(ProductRepository.class);
        resource.productRepository = repo;
        Product product = new Product();
        product.id = 1L;
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.create(product));
        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    void testUpdateWithNullNameShouldFail() {
        ProductResource resource = new ProductResource();
        ProductRepository repo = mock(ProductRepository.class);
        resource.productRepository = repo;
        Product product = new Product();
        product.name = null;
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.update(1L, product));
        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    void testUpdateProductNotFound() {
        ProductResource resource = new ProductResource();
        ProductRepository repo = mock(ProductRepository.class);
        resource.productRepository = repo;
        Product product = new Product();
        product.name = "Test";
        when(repo.findById(1L)).thenReturn(null);
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.update(1L, product));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void testDeleteProductNotFound() {
        ProductResource resource = new ProductResource();
        ProductRepository repo = mock(ProductRepository.class);
        resource.productRepository = repo;
        when(repo.findById(1L)).thenReturn(null);
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.delete(1L));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void testErrorMapperWebApplicationException() {
        ProductResource.ErrorMapper mapper = new ProductResource.ErrorMapper();
        mapper.objectMapper = new ObjectMapper();
        WebApplicationException ex = new WebApplicationException("error", Response.status(422).build());
        Response response = mapper.toResponse(ex);
        assertEquals(422, response.getStatus());
        String entity = response.getEntity().toString();
        assertTrue(entity.contains("WebApplicationException"));
        assertTrue(entity.contains("error"));
    }

    @Test
    void testErrorMapperGenericException() {
        ProductResource.ErrorMapper mapper = new ProductResource.ErrorMapper();
        mapper.objectMapper = new ObjectMapper();
        Exception ex = new Exception("generic error");
        Response response = mapper.toResponse(ex);
        assertEquals(500, response.getStatus());
        String entity = response.getEntity().toString();
        assertTrue(entity.contains("Exception"));
        assertTrue(entity.contains("generic error"));
    }
}

