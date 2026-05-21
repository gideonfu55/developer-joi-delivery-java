package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductPricingServiceTest {

    private ProductPricingService productPricingService;

    @BeforeEach
    void setUp() {
        productPricingService = new ProductPricingService();
    }

    @Test
    void shouldReturnMrpAsSellingPriceWhenDiscountIsNotAvailable() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("product101")
            .productName("Wheat Bread")
            .mrp(BigDecimal.valueOf(10.50))
            .discount(null)
            .build();

        BigDecimal sellingPrice = productPricingService.calculateSellingPrice(product);

        assertEquals(BigDecimal.valueOf(10.50).setScale(2), sellingPrice);
    }

    @Test
    void shouldDeductDiscountFromMrpWhenDiscountIsAvailable() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("product101")
            .productName("Wheat Bread")
            .mrp(BigDecimal.valueOf(10.50))
            .discount(BigDecimal.valueOf(2.00))
            .build();

        BigDecimal sellingPrice = productPricingService.calculateSellingPrice(product);

        assertEquals(BigDecimal.valueOf(8.50).setScale(2), sellingPrice);
    }

    @Test
    void shouldNotReturnNegativeSellingPriceWhenDiscountIsGreaterThanMrp() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("product101")
            .productName("Wheat Bread")
            .mrp(BigDecimal.valueOf(10.50))
            .discount(BigDecimal.valueOf(20.00))
            .build();

        BigDecimal sellingPrice = productPricingService.calculateSellingPrice(product);

        assertEquals(BigDecimal.valueOf(0.00).setScale(2), sellingPrice);
    }
}