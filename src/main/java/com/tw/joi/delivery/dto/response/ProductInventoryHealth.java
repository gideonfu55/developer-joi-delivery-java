package com.tw.joi.delivery.dto.response;

public record ProductInventoryHealth(
    String productId,
    String productName,
    int availableStock,
    int threshold,
    String inventoryStatus
) {
}
