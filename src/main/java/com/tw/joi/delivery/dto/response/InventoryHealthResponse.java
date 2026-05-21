package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.GroceryStore;

public record InventoryHealthResponse(
    GroceryStore store,
    String inventoryStatus,
    int totalProducts,
    int lowStockProductCount,
    int outOfStockProductCount
) {
}
