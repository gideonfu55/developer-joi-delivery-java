package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.dto.response.StoreInventoryHealth;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
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
     * Fetches the inventory health status for a specific grocery store.
     * The method calculates the overall inventory status of a store, the number of
     * products in various inventory states (low stock, out of stock), and compiles
     * a detailed inventory health report for all products in the store.
     *
     * @param storeId The unique identifier of the grocery store whose inventory health needs to be fetched.
     * @return An {@link StoreInventoryHealth} object containing the store details, overall inventory status,
     *         total number of products, count of low-stock products, count of out-of-stock products,
     *         and a detailed product-wise inventory health report.
     */
    public StoreInventoryHealth fetchStoreInventoryHealth(String storeId) {
        GroceryStore store = findStoreByStoreId(storeId);
        List<GroceryProduct> productsForStore = findProductsByStoreId(storeId);
        List<ProductInventoryHealth> productInventoryHealthList = productsForStore.stream()
            .map(this::toProductInventoryHealth)
            .toList();

        int totalProducts = productsForStore.size();
        int lowStockProductCount = countLowStockProducts(productsForStore);
        int outOfStockProductCount = countOutOfStockProducts(productsForStore);

        String storeInventoryStatus = calculateStoreInventoryStatus(totalProducts, lowStockProductCount, outOfStockProductCount);

        return new StoreInventoryHealth(
            store,
            storeInventoryStatus,
            totalProducts,
            lowStockProductCount,
            outOfStockProductCount,
            productInventoryHealthList
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

    private ProductInventoryHealth toProductInventoryHealth(GroceryProduct product) {
        return new ProductInventoryHealth(
            product.getProductId(),
            product.getProductName(),
            product.getAvailableStock(),
            product.getThreshold(),
            calculateProductInventoryStatus(product)
        );
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

    private String calculateProductInventoryStatus(GroceryProduct product) {
        if (product.getAvailableStock() == 0) {
            return OUT_OF_STOCK;
        }

        if (product.getAvailableStock() <= product.getThreshold()) {
            return LOW_STOCK;
        }

        return HEALTHY;
    }

    private String calculateStoreInventoryStatus(
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
