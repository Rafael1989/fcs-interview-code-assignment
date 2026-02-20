package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseProductStoreAssociationRepository;
import com.fulfilment.application.monolith.fulfillment.domain.models.WarehouseProductStoreAssociation;
import com.fulfilment.application.monolith.fulfillment.domain.usecases.AssociateWarehouseToProductInStoreUseCase;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FulfillmentResourceImplTest {

    private FulfillmentResourceImpl resource;
    private AssociateWarehouseToProductInStoreUseCase useCase;
    private WarehouseProductStoreAssociationRepository repo;

    @BeforeEach
    void setup() throws Exception {
        resource = new FulfillmentResourceImpl();
        useCase = mock(AssociateWarehouseToProductInStoreUseCase.class);
        repo = mock(WarehouseProductStoreAssociationRepository.class);

        // Usando reflexão para injetar os mocks nos campos privados
        Field useCaseField = FulfillmentResourceImpl.class.getDeclaredField("associateUseCase");
        useCaseField.setAccessible(true);
        useCaseField.set(resource, useCase);

        Field repoField = FulfillmentResourceImpl.class.getDeclaredField("associationRepository");
        repoField.setAccessible(true);
        repoField.set(resource, repo);
    }

    @Test
    void testAssociate() {
        WarehouseProductStoreAssociation assoc = new WarehouseProductStoreAssociation();
        when(useCase.associate("W1", 1L, 2L)).thenReturn(assoc);
        Response response = resource.associate("W1", 1L, 2L);
        assertEquals(201, response.getStatus());
        assertEquals(assoc, response.getEntity());
    }

    @Test
    void testGetAssociationsByWarehouse() {
        when(repo.findByWarehouse("W1")).thenReturn(Collections.emptyList());
        assertNotNull(resource.getAssociationsByWarehouse("W1"));
    }

    @Test
    void testGetAssociationsByStore() {
        when(repo.findByStore(2L)).thenReturn(Collections.emptyList());
        assertNotNull(resource.getAssociationsByStore(2L));
    }

    @Test
    void testGetAssociationsByProductStore() {
        when(repo.findByProductAndStore(1L, 2L)).thenReturn(Collections.emptyList());
        assertNotNull(resource.getAssociationsByProductStore(1L, 2L));
    }
}
