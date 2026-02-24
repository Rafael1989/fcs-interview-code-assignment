package com.fulfilment.application.monolith.stores;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
/**
 * Listener for Store events that triggers legacy system updates.
 * This listener observes events AFTER the transaction successfully commits,
 * ensuring that the legacy system is only notified when data is safely persisted.
 */
@ApplicationScoped
public class StoreEventListener {
  private static final Logger LOGGER = Logger.getLogger(StoreEventListener.class.getName());
  @Inject
  LegacyStoreManagerGateway legacyStoreManagerGateway;
  /**
   * Handles store events after successful transaction commit.
   * This ensures the legacy system is only updated when the database transaction succeeds.
   * 
   * @param event the store event containing operation details
   */
  public void onStoreEvent(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreEvent event) {
    try {
      Store store = event.getStore();
      LOGGER.info("Processing store event: " + event.getEventType() + " for store: " + store.name);
      switch (event.getEventType()) {
        case CREATED:
          legacyStoreManagerGateway.createStoreOnLegacySystem(store);
          LOGGER.info("Successfully notified legacy system of store creation: " + store.name);
          break;
        case UPDATED:
          legacyStoreManagerGateway.updateStoreOnLegacySystem(store);
          LOGGER.info("Successfully notified legacy system of store update: " + store.name);
          break;
        default:
          LOGGER.warn("Unknown event type: " + event.getEventType());
      }
    } catch (Exception e) {
      // Log the error but don't fail the request since DB transaction already committed
      LOGGER.error("Failed to notify legacy system: " + e.getMessage(), e);
    }
  }
}
