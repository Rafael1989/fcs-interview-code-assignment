package com.fulfilment.application.monolith.stores;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class StoreEventListenerTest {
  @Mock
  private LegacyStoreManagerGateway legacyStoreManagerGateway;
  @InjectMocks
  private StoreEventListener listener;
  private Store testStore;
  @BeforeEach
  void setUp() {
    testStore = new Store();
    testStore.id = 1L;
    testStore.name = "Test Store";
    testStore.quantityProductsInStock = 100;
  }
  @Test
  void testOnStoreEventCreated() {
    // Given
    StoreEvent event = new StoreEvent(testStore, StoreEvent.EventType.CREATED);
    // When
    listener.onStoreEvent(event);
    // Then
    verify(legacyStoreManagerGateway, times(1)).createStoreOnLegacySystem(testStore);
    verify(legacyStoreManagerGateway, never()).updateStoreOnLegacySystem(any());
  }
  @Test
  void testOnStoreEventUpdated() {
    // Given
    StoreEvent event = new StoreEvent(testStore, StoreEvent.EventType.UPDATED);
    // When
    listener.onStoreEvent(event);
    // Then
    verify(legacyStoreManagerGateway, times(1)).updateStoreOnLegacySystem(testStore);
    verify(legacyStoreManagerGateway, never()).createStoreOnLegacySystem(any());
  }
  @Test
  void testOnStoreEventCreatedWithException() {
    // Given
    StoreEvent event = new StoreEvent(testStore, StoreEvent.EventType.CREATED);
    doThrow(new RuntimeException("Legacy system error"))
        .when(legacyStoreManagerGateway)
        .createStoreOnLegacySystem(testStore);
    // When - should not throw exception
    listener.onStoreEvent(event);
    // Then
    verify(legacyStoreManagerGateway, times(1)).createStoreOnLegacySystem(testStore);
  }
  @Test
  void testOnStoreEventUpdatedWithException() {
    // Given
    StoreEvent event = new StoreEvent(testStore, StoreEvent.EventType.UPDATED);
    doThrow(new RuntimeException("Legacy system error"))
        .when(legacyStoreManagerGateway)
        .updateStoreOnLegacySystem(testStore);
    // When - should not throw exception
    listener.onStoreEvent(event);
    // Then
    verify(legacyStoreManagerGateway, times(1)).updateStoreOnLegacySystem(testStore);
  }
}
