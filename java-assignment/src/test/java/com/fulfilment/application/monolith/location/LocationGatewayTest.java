package com.fulfilment.application.monolith.location;

import static org.junit.jupiter.api.Assertions.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationGatewayTest {

  private LocationGateway locationGateway;

  @BeforeEach
  void setup() {
    locationGateway = new LocationGateway();
  }

  @Test
  public void testResolveExistingLocationShouldReturn() {
    // given
    String locationId = "ZWOLLE-001";

    // when
    Location location = locationGateway.resolveByIdentifier(locationId);

    // then
    assertNotNull(location);
    assertEquals("ZWOLLE-001", location.identification);
    assertEquals(1, location.maxNumberOfWarehouses);
    assertEquals(40, location.maxCapacity);
  }

  @Test
  public void testResolveAnotherLocationShouldReturn() {
    // given
    String locationId = "AMSTERDAM-001";

    // when
    Location location = locationGateway.resolveByIdentifier(locationId);

    // then
    assertNotNull(location);
    assertEquals("AMSTERDAM-001", location.identification);
    assertEquals(5, location.maxNumberOfWarehouses);
    assertEquals(100, location.maxCapacity);
  }

  @Test
  public void testResolveInvalidLocationShouldThrow() {
    // given
    String locationId = "INVALID-999";

    // when & then
    assertThrows(
        IllegalArgumentException.class,
        () -> locationGateway.resolveByIdentifier(locationId),
        "Should throw exception for non-existent location");
  }
}
