package com.fulfilment.application.monolith.fulfillment.adapters;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fulfilment.application.monolith.fulfillment.adapters.restapi.FulfillmentResourceImpl;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FulfillmentErrorMapperTest {

  private ObjectMapper objectMapper;
  private FulfillmentResourceImpl.ErrorMapper errorMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    errorMapper = new FulfillmentResourceImpl.ErrorMapper();
    try {
      var field = errorMapper.getClass().getDeclaredField("objectMapper");
      field.setAccessible(true);
      field.set(errorMapper, objectMapper);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testToResponseWithWebApplicationException() {
    // Arrange
    WebApplicationException exception = new WebApplicationException("Not found", 404);

    // Act
    Response response = errorMapper.toResponse(exception);

    // Assert
    assertEquals(404, response.getStatus());
    assertNotNull(response.getEntity());
    assertTrue(response.getEntity() instanceof ObjectNode);
  }

  @Test
  void testToResponseWithGenericException() {
    // Arrange
    Exception exception = new Exception("Some error");

    // Act
    Response response = errorMapper.toResponse(exception);

    // Assert
    assertEquals(500, response.getStatus());
    assertNotNull(response.getEntity());
    assertTrue(response.getEntity() instanceof ObjectNode);
  }

  @Test
  void testToResponseWithNullMessage() {
    // Arrange
    Exception exception = new Exception();

    // Act
    Response response = errorMapper.toResponse(exception);

    // Assert
    assertEquals(500, response.getStatus());
    assertNotNull(response.getEntity());
    assertTrue(response.getEntity() instanceof ObjectNode);
  }

  @Test
  void testToResponseWithWebApplicationExceptionMessage() {
    // Arrange
    WebApplicationException exception = new WebApplicationException("Bad Request", 400);

    // Act
    Response response = errorMapper.toResponse(exception);

    // Assert
    assertEquals(400, response.getStatus());
    ObjectNode entity = (ObjectNode) response.getEntity();
    assertNotNull(entity);
    assertTrue(entity.has("exceptionType"));
    assertTrue(entity.has("code"));
  }
}

