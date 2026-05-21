package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductPricingService {
    public BigDecimal calculateSellingPrice(GroceryProduct product) {
        // Calculate the selling price based on MRP, discounts, and other factors
        BigDecimal mrp = product.getMrp();
        BigDecimal discount = product.getDiscount() != null ? product.getDiscount() : BigDecimal.ZERO;

        BigDecimal sellingPrice = mrp.subtract(discount);

        if (sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return sellingPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
