package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import com.tw.joi.delivery.seedData.SeedData;

import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final Map<String,Cart> userCarts= SeedData.cartForUsers;
    private final UserService userService;
    private final ProductService productService;
    private final ProductPricingService productPricingService;

    public CartProductInfo addProductToCartForUser(AddProductRequest addProductRequest) {
        User user=userService.fetchUserById(addProductRequest.getUserId());
        Cart cart = fetchCartForUser(user);
        GroceryProduct product = productService.getProduct(addProductRequest.getProductId(),
                                                           addProductRequest.getOutletId());

        BigDecimal sellingPrice = productPricingService.calculateSellingPrice(product);
        product.setSellingPrice(sellingPrice);

        cart.getProducts().add(product);
        return new CartProductInfo(cart, product, sellingPrice);
    }

    public Cart getCartForUser(String userId) {
        User user=userService.fetchUserById(userId);
        return fetchCartForUser(user);
    }

    private Cart fetchCartForUser(User user) {
        return userCarts.get(user.getUserId());
    }

}
