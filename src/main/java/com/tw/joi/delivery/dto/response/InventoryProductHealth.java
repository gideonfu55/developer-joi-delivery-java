package com.tw.joi.delivery.dto.response;

public record InventoryProductHealth(
    String productId,
    String productName,
    int availableStock,
    int threshold,
    String inventoryStatus
) {
}
