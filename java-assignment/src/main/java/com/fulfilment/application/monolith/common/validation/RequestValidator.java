package com.fulfilment.application.monolith.common.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

/**
 * Centralized validator for common request validation logic.
 * This class provides reusable validation methods to ensure consistency
 * across different resource endpoints.
 */
@ApplicationScoped
public class RequestValidator {

  /**
   * Validates that a string ID can be parsed to a Long.
   *
   * @param id the string ID to validate
   * @param resourceType the type of resource (for error messages)
   * @return the parsed Long ID
   * @throws WebApplicationException if the ID format is invalid
   */
  public Long validateAndParseLongId(String id, String resourceType) {
    if (id == null || id.trim().isEmpty()) {
      throw new WebApplicationException(
          resourceType + " ID cannot be null or empty", 400);
    }

    try {
      return Long.parseLong(id);
    } catch (NumberFormatException e) {
      throw new WebApplicationException(
          "Invalid " + resourceType + " ID format: " + id + ". Expected a numeric value.", 400);
    }
  }

  /**
   * Validates that a string field is not null or empty.
   *
   * @param value the value to validate
   * @param fieldName the name of the field (for error messages)
   * @throws WebApplicationException if the field is null or empty
   */
  public void validateNotEmpty(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new WebApplicationException(
          fieldName + " cannot be null or empty", 422);
    }
  }

  /**
   * Validates that a numeric value is positive.
   *
   * @param value the value to validate
   * @param fieldName the name of the field (for error messages)
   * @throws WebApplicationException if the value is not positive
   */
  public void validatePositive(Integer value, String fieldName) {
    if (value == null || value <= 0) {
      throw new WebApplicationException(
          fieldName + " must be a positive number", 422);
    }
  }

  /**
   * Validates that a numeric value is non-negative.
   *
   * @param value the value to validate
   * @param fieldName the name of the field (for error messages)
   * @throws WebApplicationException if the value is negative
   */
  public void validateNonNegative(Integer value, String fieldName) {
    if (value == null || value < 0) {
      throw new WebApplicationException(
          fieldName + " must be non-negative", 422);
    }
  }

  /**
   * Validates that an object is not null.
   *
   * @param object the object to validate
   * @param objectName the name of the object (for error messages)
   * @throws WebApplicationException if the object is null
   */
  public void validateNotNull(Object object, String objectName) {
    if (object == null) {
      throw new WebApplicationException(
          objectName + " cannot be null", 422);
    }
  }

  /**
   * Validates that an ID is not set on a create request.
   *
   * @param id the ID to check
   * @param resourceType the type of resource (for error messages)
   * @throws WebApplicationException if the ID is set
   */
  public void validateIdNotSet(Long id, String resourceType) {
    if (id != null) {
      throw new WebApplicationException(
          "ID was invalidly set on " + resourceType + " create request.", 422);
    }
  }

  /**
   * Validates business unit code format.
   *
   * @param businessUnitCode the business unit code to validate
   * @throws WebApplicationException if the format is invalid
   */
  public void validateBusinessUnitCode(String businessUnitCode) {
    validateNotEmpty(businessUnitCode, "Business Unit Code");

    if (!businessUnitCode.matches("^[A-Z0-9-]+$")) {
      throw new WebApplicationException(
          "Business Unit Code must contain only uppercase letters, numbers, and hyphens", 422);
    }
  }
}

