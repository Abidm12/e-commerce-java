package service;

import model.OrderItem;
import model.Product;

import java.math.BigDecimal;
import java.util.*;

public class CartService {

    private Map<Product, Integer> cart;

    public CartService() {
        cart = new HashMap<>();
    }

    // Add Product to Cart
    public void addToCart(Product product, int quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        cart.merge(product, quantity, Integer::sum);

        System.out.println("Added to cart.");
    }

    
    // Remove Product
    public void removeFromCart(Product product) {
        cart.remove(product);
        System.out.println("Removed from cart.");
    }

    
    // View Car
    public void viewCart() {

        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\n=== CART ===");

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            System.out.println(
                    product.getId() + " | " +
                            product.getName() + " | " +
                            product.getPrice() + " | Qty: " +
                            quantity
            );
        }

        System.out.println("Total: " + calculateTotal());
    }

    // Calculate Total
    public BigDecimal calculateTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {

            BigDecimal price = entry.getKey().getPrice();
            int quantity = entry.getValue();

            total = total.add(
                    price.multiply(BigDecimal.valueOf(quantity))
            );
        }

        return total;
    }

=
    // Convert Cart -> OrderItems
    public List<OrderItem> getOrderItems() {

        List<OrderItem> items = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {

            Product product = entry.getKey();
            int quantity = entry.getValue();

            items.add(new OrderItem(
                    product.getId(),
                    quantity,
                    product.getPrice()
            ));
        }

        return items;
    }

  
    // Clear Cart
    public void clearCart() {
        cart.clear();
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }
}
