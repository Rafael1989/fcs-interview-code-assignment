package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FulfillmentResourceImplBranchTest {

    @Test
    void errorMapperHandlesWebApplicationException() {
        FulfillmentResourceImpl.ErrorMapper mapper = new FulfillmentResourceImpl.ErrorMapper();
        mapper.objectMapper = new ObjectMapper();
        WebApplicationException ex = new WebApplicationException("error", Response.status(422).build());
        Response response = mapper.toResponse(ex);
        assertEquals(422, response.getStatus());
        String entity = response.getEntity().toString();
        assertTrue(entity.contains("WebApplicationException"));
        assertTrue(entity.contains("error"));
    }

    @Test
    void errorMapperHandlesGenericException() {
        FulfillmentResourceImpl.ErrorMapper mapper = new FulfillmentResourceImpl.ErrorMapper();
        mapper.objectMapper = new ObjectMapper();
        Exception ex = new Exception("generic error");
        Response response = mapper.toResponse(ex);
        assertEquals(500, response.getStatus());
        String entity = response.getEntity().toString();
        assertTrue(entity.contains("Exception"));
        assertTrue(entity.contains("generic error"));
    }
}

