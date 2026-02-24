package com.fulfilment.application.monolith.stores;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class StoreEventTest {
  @Test
  void testStoreEventCreation() {
    // Given
    Store store = new Store();
    store.id = 1L;
    store.name = "Test Store";
    store.quantityProductsInStock = 50;
    // When
    StoreEvent event = new StoreEvent(store, StoreEvent.EventType.CREATED);
    // Then
    assertNotNull(event);
    assertEquals(store, event.getStore());
    assertEquals(StoreEvent.EventType.CREATED, event.getEventType());
  }
  @Test
  void testStoreEventWithUpdatedType() {
    // Given
    Store store = new Store();
    store.id = 2L;
    store.name = "Updated Store";
    store.quantityProductsInStock = 100;
    // When
    StoreEvent event = new StoreEvent(store, StoreEvent.EventType.UPDATED);
    // Then
    assertNotNull(event);
    assertEquals(store, event.getStore());
    assertEquals(StoreEvent.EventType.UPDATED, event.getEventType());
  }
  @Test
  void testEventTypeEnum() {
    // Test that both event types exist
    assertNotNull(StoreEvent.EventType.CREATED);
    assertNotNull(StoreEvent.EventType.UPDATED);
    // Test enum values
    StoreEvent.EventType[] types = StoreEvent.EventType.values();
    assertEquals(2, types.length);
  }
}
