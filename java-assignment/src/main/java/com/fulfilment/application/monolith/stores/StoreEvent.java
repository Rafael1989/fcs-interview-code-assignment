package com.fulfilment.application.monolith.stores;
/**
 * Event fired after a Store operation is successfully committed to the database.
 * This ensures that downstream systems are notified only after data is persisted.
 */
public class StoreEvent {
  public enum EventType {
    CREATED,
    UPDATED
  }
  private final Store store;
  private final EventType eventType;
  public StoreEvent(Store store, EventType eventType) {
    this.store = store;
    this.eventType = eventType;
  }
  public Store getStore() {
    return store;
  }
  public EventType getEventType() {
    return eventType;
  }
}
