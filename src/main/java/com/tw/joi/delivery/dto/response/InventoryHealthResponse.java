package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.GroceryStore;

import java.util.List;

public record InventoryHealthResponse(
    GroceryStore store,
    String overallStoreInventoryStatus,
    int totalProducts,
    int lowStockProductCount,
    int outOfStockProductCount,
    List<InventoryProductHealth> products
) {
}
