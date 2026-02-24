package com.fulfilment.application.monolith.common.validation;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestValidatorTest {

  private RequestValidator validator;

  @BeforeEach
  void setUp() {
    validator = new RequestValidator();
  }

  @Test
  void testValidateAndParseLongId_ValidId() {
    Long result = validator.validateAndParseLongId("123", "Product");
    assertEquals(123L, result);
  }

  @Test
  void testValidateAndParseLongId_InvalidFormat() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class,
            () -> validator.validateAndParseLongId("abc", "Product"));
    assertEquals(400, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("Invalid Product ID format"));
  }

  @Test
  void testValidateAndParseLongId_NullId() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateAndParseLongId(null, "Store"));
    assertEquals(400, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("ID cannot be null or empty"));
  }

  @Test
  void testValidateAndParseLongId_EmptyId() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateAndParseLongId("  ", "Store"));
    assertEquals(400, exception.getResponse().getStatus());
  }

  @Test
  void testValidateNotEmpty_ValidValue() {
    assertDoesNotThrow(() -> validator.validateNotEmpty("test", "Field"));
  }

  @Test
  void testValidateNotEmpty_NullValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateNotEmpty(null, "Username"));
    assertEquals(422, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("Username cannot be null or empty"));
  }

  @Test
  void testValidateNotEmpty_EmptyValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateNotEmpty("  ", "Email"));
    assertEquals(422, exception.getResponse().getStatus());
  }

  @Test
  void testValidatePositive_ValidValue() {
    assertDoesNotThrow(() -> validator.validatePositive(10, "Quantity"));
  }

  @Test
  void testValidatePositive_ZeroValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validatePositive(0, "Price"));
    assertEquals(422, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("Price must be a positive number"));
  }

  @Test
  void testValidatePositive_NegativeValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validatePositive(-5, "Stock"));
    assertEquals(422, exception.getResponse().getStatus());
  }

  @Test
  void testValidatePositive_NullValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validatePositive(null, "Amount"));
    assertEquals(422, exception.getResponse().getStatus());
  }

  @Test
  void testValidateNonNegative_ValidValue() {
    assertDoesNotThrow(() -> validator.validateNonNegative(0, "Balance"));
    assertDoesNotThrow(() -> validator.validateNonNegative(10, "Count"));
  }

  @Test
  void testValidateNonNegative_NegativeValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateNonNegative(-1, "Discount"));
    assertEquals(422, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("Discount must be non-negative"));
  }

  @Test
  void testValidateNonNegative_NullValue() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateNonNegative(null, "Points"));
    assertEquals(422, exception.getResponse().getStatus());
  }

  @Test
  void testValidateNotNull_ValidObject() {
    assertDoesNotThrow(() -> validator.validateNotNull(new Object(), "Request"));
  }

  @Test
  void testValidateNotNull_NullObject() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateNotNull(null, "Payload"));
    assertEquals(422, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("Payload cannot be null"));
  }

  @Test
  void testValidateIdNotSet_NullId() {
    assertDoesNotThrow(() -> validator.validateIdNotSet(null, "Product"));
  }

  @Test
  void testValidateIdNotSet_IdIsSet() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateIdNotSet(123L, "Warehouse"));
    assertEquals(422, exception.getResponse().getStatus());
    assertTrue(exception.getMessage().contains("ID was invalidly set on Warehouse create request"));
  }

  @Test
  void testValidateBusinessUnitCode_ValidCode() {
    assertDoesNotThrow(() -> validator.validateBusinessUnitCode("AMSTERDAM-001"));
    assertDoesNotThrow(() -> validator.validateBusinessUnitCode("NYC123"));
    assertDoesNotThrow(() -> validator.validateBusinessUnitCode("STORE-456-A"));
  }

  @Test
  void testValidateBusinessUnitCode_NullCode() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateBusinessUnitCode(null));
    assertEquals(422, exception.getResponse().getStatus());
  }

  @Test
  void testValidateBusinessUnitCode_EmptyCode() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateBusinessUnitCode("  "));
    assertEquals(422, exception.getResponse().getStatus());
  }

  @Test
  void testValidateBusinessUnitCode_InvalidFormat() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateBusinessUnitCode("invalid_code"));
    assertEquals(422, exception.getResponse().getStatus());
    assertTrue(
        exception
            .getMessage()
            .contains("Business Unit Code must contain only uppercase letters, numbers, and hyphens"));
  }

  @Test
  void testValidateBusinessUnitCode_LowercaseCharacters() {
    WebApplicationException exception =
        assertThrows(
            WebApplicationException.class, () -> validator.validateBusinessUnitCode("amsterdam-001"));
    assertEquals(422, exception.getResponse().getStatus());
  }
}

