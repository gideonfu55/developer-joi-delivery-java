package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.exception.ResourceNotFoundException;
import com.tw.joi.delivery.seedData.SeedData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class InventoryService {

    private static final String HEALTHY = "HEALTHY";
    private static final String LOW_STOCK = "LOW_STOCK";
    private static final String OUT_OF_STOCK = "OUT_OF_STOCK";
    private static final String EMPTY = "EMPTY";

    /**
     * Fetches the inventory health for a specific grocery store by its store ID.
     *
     * This method determines the inventory health based on the total number of products,
     * the count of products with low stock, and the count of products that are out of stock.
     * It calculates and returns an inventory status such as "HEALTHY", "LOW_STOCK", "OUT_OF_STOCK", or "EMPTY".
     *
     * @param storeId the unique identifier of the grocery store for which the inventory health is to be fetched
     * @return an {@code InventoryHealthResponse} object containing the store details, inventory status,
     *         total number of products, count of low-stock products, and count of out-of-stock products
     */
    public InventoryHealthResponse fetchStoreInventoryHealth(String storeId) {
        GroceryStore store = findStoreByStoreId(storeId);
        List<GroceryProduct> productsForStore = findProductsByStoreId(storeId);

        int totalProducts = productsForStore.size();
        int lowStockProductCount = countLowStockProducts(productsForStore);
        int outOfStockProductCount = countOutOfStockProducts(productsForStore);

        String inventoryStatus = calculateInventoryStatus(totalProducts, lowStockProductCount, outOfStockProductCount);

        return new InventoryHealthResponse(
            store,
            inventoryStatus,
            totalProducts,
            lowStockProductCount,
            outOfStockProductCount
        );
    }

    private GroceryStore findStoreByStoreId(String storeId) {
        return Stream.of(SeedData.store101, SeedData.store102)
            .filter(store -> store.getOutletId().equals(storeId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException(
                "Store not found for storeId: " + storeId
            ));
    }

    private List<GroceryProduct> findProductsByStoreId(String storeId) {
        return SeedData.groceryProducts.stream()
            .filter(Objects::nonNull)
            .filter(product -> product.getStore() != null)
            .filter(product -> product.getStore().getOutletId().equals(storeId))
            .toList();
    }

    private int countLowStockProducts(List<GroceryProduct> products) {
        return (int) products.stream()
            .filter(product -> product.getAvailableStock() > 0)
            .filter(product -> product.getAvailableStock() <= product.getThreshold())
            .count();
    }

    private int countOutOfStockProducts(List<GroceryProduct> products) {
        return (int) products.stream()
            .filter(product -> product.getAvailableStock() == 0)
            .count();
    }

    private String calculateInventoryStatus(
        int totalProducts,
        int lowStockProductCount,
        int outOfStockProductCount) {

        if (totalProducts == 0) {
            return EMPTY;
        }

        if (outOfStockProductCount > 0) {
            return OUT_OF_STOCK;
        }

        if (lowStockProductCount > 0) {
            return LOW_STOCK;
        }

        return HEALTHY;
    }
}
