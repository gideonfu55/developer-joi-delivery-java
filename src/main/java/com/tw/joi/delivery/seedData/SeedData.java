package com.tw.joi.delivery.seedData;

import com.tw.joi.delivery.domain.*;

import java.math.BigDecimal;
import java.util.*;

public class SeedData {

    public static GroceryStore store101 = SeedData.createStore("Fresh Picks", "store101");
    public static GroceryStore store102 = SeedData.createStore("Natural Choice", "store102");

    public static User user101= SeedData.createUser("user101", "John", "Doe");

    public static List<GroceryProduct> groceryProducts =
        Arrays.asList(createGroceryProduct("Wheat Bread", "product101", store101),
            createGroceryProduct("Spinach", "product102", store101),
            createGroceryProductWithDiscount("Crackers", "product103", store101));

    public static Map<String, Cart> cartForUsers = Map.of(
        "user101", createCartForUser("user101", "John", "Doe", "cart101"),
        "user102", createCartForUser("user102", "Rachel", "Zane", "cart102"));

    public static List<User> users = Arrays.asList(user101);

    public static Cart createCartForUser(String userId, String firstName, String lastName,
                                         String cartId) {

        List<Product> groceryProductsUser101 = Collections.singletonList(groceryProducts.stream()
            .map(Product.class::cast)
            .toList()
            .getFirst()
        );

        return Cart.builder()
            .cartId(cartId)
            .outlet(store101)
            .user(user101)
            .products(groceryProductsUser101)
            .build();
    }

    public static GroceryStore createStore(String outletName, String storeId) {
        return GroceryStore.builder()
            .name(outletName)
            .outletId(storeId)
            .build();
    }

    public static User createUser(String userId, String firstName, String lastName) {
        return User.builder()
            .userId(userId)
            .firstName(firstName)
            .lastName(lastName)
            .email(firstName + "." + lastName + "@gmail.com")
            .phoneNumber(String.valueOf(SeedData.getRandomNumberUsingNextInt(100000000, 900000000)))
            .build();
    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private static GroceryProduct createGroceryProduct(String productName,
                                                       String productId, GroceryStore store) {
        return GroceryProduct.builder()
            .productName(productName)
            .productId(productId)
            .mrp(BigDecimal.valueOf(10.5))
            .weight(BigDecimal.valueOf(500.00))
            .store(store)
            .threshold(10)
            .availableStock(30)
//            .availableStock(0) // to test out-of-stock scenario
            .build();
    }

    private static GroceryProduct createGroceryProductWithDiscount(String productName,
                                                       String productId, GroceryStore store) {

        GroceryProduct product = createGroceryProduct(productName, productId, store);
        product.setDiscount(BigDecimal.valueOf(2.00));
        return product;
    }

}
