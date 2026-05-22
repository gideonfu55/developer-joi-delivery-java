package com.tw.joi.delivery.service;

import com.tw.joi.delivery.dto.response.StoreInventoryHealth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService();
    }

    @Test
    void shouldReturnHealthyInventoryForStore101() {
        StoreInventoryHealth response =
            inventoryService.fetchStoreInventoryHealth("store101");

        assertEquals("store101", response.store().getOutletId());
        assertEquals("LOW_STOCK", response.overallStoreInventoryStatus());
        assertEquals(3, response.totalProducts());
        assertEquals(3, response.lowStockProductCount());
        assertEquals(0, response.outOfStockProductCount());
        assertEquals(3, response.products().size());

        assertEquals("LOW_STOCK", response.products().get(0).inventoryStatus());
    }
}