package main;

import model.Product;
import model.Role;
import model.User;
import service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        UserService userService = new UserService();
        ProductService productService = new ProductService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();

        User loggedInUser = null;

        while (true) {

            // =========================
            // AUTH MENU
            // =========================
            if (loggedInUser == null) {

                System.out.println("\n=== AUTH MENU ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1:
                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        System.out.print("Password: ");
                        String password = scanner.nextLine();

                        userService.registerUser(name, email, password);
                        break;

                    case 2:
                        System.out.print("Email: ");
                        String loginEmail = scanner.nextLine();

                        System.out.print("Password: ");
                        String loginPassword = scanner.nextLine();

                        loggedInUser =
                                userService.loginUser(loginEmail, loginPassword);

                        if (loggedInUser != null) {
                            System.out.println("Login successful! Role: "
                                    + loggedInUser.getRole());
                        } else {
                            System.out.println("Invalid credentials.");
                        }
                        break;

                    case 3:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            }
            // =========================
            // ADMIN MENU
            // =========================
            else if (loggedInUser.getRole() == Role.ADMIN) {

                System.out.println("\n=== ADMIN MENU ===");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. View Revenue");
                System.out.println("4. Logout");
                System.out.print("Enter choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1:
                        System.out.print("Product Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Price: ");
                        BigDecimal price =
                                new BigDecimal(scanner.nextLine());

                        System.out.print("Stock: ");
                        int stock = scanner.nextInt();
                        scanner.nextLine();

                        boolean added =
                                productService.addProduct(name, price, stock);

                        if (added) {
                            System.out.println("Product added successfully.");
                        }
                        break;

                    case 2:
                        List<Product> products =
                                productService.getAllProducts();

                        for (Product p : products) {
                            System.out.println(
                                    p.getId() + " | " +
                                            p.getName() + " | ₹" +
                                            p.getPrice() + " | Stock: " +
                                            p.getStock()
                            );
                        }
                        break;

                    case 3:
                        // Simple revenue calculation
                        orderService.viewTotalRevenue();
                        break;

                    case 4:
                        loggedInUser = null;
                        cartService.clearCart();
                        System.out.println("Logged out.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
            // =========================
            // USER MENU
            // =========================
            else {

                System.out.println("\n=== USER MENU ===");
                System.out.println("1. View Products");
                System.out.println("2. Add to Cart");
                System.out.println("3. View Cart");
                System.out.println("4. Checkout");
                System.out.println("5. View Order History");
                System.out.println("6. Logout");
                System.out.print("Enter choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1:
                        List<Product> products =
                                productService.getAllProducts();

                        for (Product p : products) {
                            System.out.println(
                                    p.getId() + " | " +
                                            p.getName() + " | ₹" +
                                            p.getPrice() + " | Stock: " +
                                            p.getStock()
                            );
                        }
                        break;

                    case 2:
                        System.out.print("Enter Product ID: ");
                        int productId = scanner.nextInt();

                        System.out.print("Quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        Product product =
                                productService.findProductById(productId);

                        if (product != null) {
                            cartService.addToCart(product, quantity);
                        } else {
                            System.out.println("Product not found.");
                        }
                        break;

                    case 3:
                        cartService.viewCart();
                        break;

                    case 4:
                        if (cartService.isEmpty()) {
                            System.out.println("Cart is empty.");
                            break;
                        }

                        BigDecimal total =
                                cartService.calculateTotal();

                        boolean success =
                                orderService.placeOrder(
                                        loggedInUser.getId(),
                                        total,
                                        cartService.getOrderItems()
                                );

                        if (success) {
                            System.out.println("Order placed successfully!");
                            cartService.clearCart();
                        } else {
                            System.out.println("Order failed.");
                        }
                        break;

                    case 5:
                        orderService.viewOrderHistory(
                                loggedInUser.getId());
                        break;

                    case 6:
                        loggedInUser = null;
                        cartService.clearCart();
                        System.out.println("Logged out.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
    }
}
