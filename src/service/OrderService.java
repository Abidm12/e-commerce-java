package service;

import dao.OrderDAO;
import model.OrderItem;
import model.Product;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    private OrderDAO orderDAO;
    private ProductService productService;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.productService = new ProductService();
    }

    public boolean placeOrder(int userId,
                              BigDecimal totalAmount,
                              List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            System.out.println("Cart is empty.");
            return false;
        }

        // Stock validation BEFORE transaction
        for (OrderItem item : items) {

            Product product =
                    productService.findProductById(item.getProductId());

            if (product == null) {
                System.out.println("Product not found: " + item.getProductId());
                return false;
            }

            if (product.getStock() < item.getQuantity()) {
                System.out.println("Insufficient stock for product: "
                        + product.getName());
                return false;
            }
        }

        return orderDAO.createOrder(userId, totalAmount, items);
    }

    public void viewOrderHistory(int userId) {
        orderDAO.viewOrderHistory(userId);
    }

    public void viewTotalRevenue() {
        orderDAO.viewTotalRevenue();
    }


}
