package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.GroceryStore;

import java.util.List;

public record StoreInventoryHealth(
    GroceryStore store,
    String overallStoreInventoryStatus,
    int totalProducts,
    int lowStockProductCount,
    int outOfStockProductCount,
    List<ProductInventoryHealth> products
) {
}
