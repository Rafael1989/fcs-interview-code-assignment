package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class WarehouseErrorMapper implements ExceptionMapper<Exception> {
  private static final Logger LOGGER = Logger.getLogger(WarehouseErrorMapper.class.getName());

  @Inject public ObjectMapper objectMapper;

  @Override
  public Response toResponse(Exception exception) {
    LOGGER.warning("Failed to handle warehouse request: " + exception.getClass().getSimpleName() + " - " + exception.getMessage());

    int code = 500;
    String message = exception.getMessage();

    if (exception instanceof WebApplicationException) {
      WebApplicationException webEx = (WebApplicationException) exception;
      code = webEx.getResponse().getStatus();
      // Extract the actual error message if it's available
      if (message == null || message.startsWith("HTTP")) {
        message = extractErrorMessage(webEx);
      }
    } else if (exception instanceof ConstraintViolationException) {
      code = 400;
      message = "Validation error: One or more fields are invalid";
    } else if (exception instanceof IllegalArgumentException) {
      code = 422;
    } else if (exception.getMessage() != null && exception.getMessage().contains("JSON")) {
      code = 400;
      message = "Invalid JSON format in request body";
    }

    ObjectNode exceptionJson = objectMapper.createObjectNode();
    exceptionJson.put("exceptionType", exception.getClass().getName());
    exceptionJson.put("code", code);

    if (message != null && !message.isEmpty()) {
      exceptionJson.put("error", message);
    } else {
      exceptionJson.put("error", "An error occurred");
    }

    return Response.status(code).entity(exceptionJson).build();
  }

  private String extractErrorMessage(WebApplicationException exception) {
    try {
      Object entity = exception.getResponse().getEntity();
      if (entity != null) {
        return entity.toString();
      }
    } catch (Exception e) {
      LOGGER.fine("Could not extract error from WebApplicationException entity: " + e.getMessage());
    }
    return exception.getMessage();
  }
}

