package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;

class LegacyStoreManagerGatewayTest {

    @Test
    void testCreateStoreOnLegacySystem() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store();
        store.name = "TestStore";
        store.quantityProductsInStock = 10;
        gateway.createStoreOnLegacySystem(store);
    }

    @Test
    void testUpdateStoreOnLegacySystem() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store();
        store.name = "UpdateStore";
        store.quantityProductsInStock = 5;
        gateway.updateStoreOnLegacySystem(store);
    }

    @Test
    void testWriteToFileException() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store();
        store.name = ""; // Nome inválido pode causar IOException
        store.quantityProductsInStock = 1;
        gateway.createStoreOnLegacySystem(store); // Deve capturar exceção internamente
    }
}
