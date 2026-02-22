package dao;

import model.OrderItem;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class OrderDAO {

    public boolean createOrder(int userId,
                               BigDecimal totalAmount,
                               List<OrderItem> items) {

        String orderSQL =
                "INSERT INTO orders (user_id, total_amount) VALUES (?, ?) RETURNING id";

        String orderItemSQL =
                "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        String updateStockSQL =
                "UPDATE products SET stock = stock - ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false); // START TRANSACTION

            //Insert Order
            int orderId;

            try (PreparedStatement orderStmt =
                         conn.prepareStatement(orderSQL)) {

                orderStmt.setInt(1, userId);
                orderStmt.setBigDecimal(2, totalAmount);

                ResultSet rs = orderStmt.executeQuery();
                rs.next();
                orderId = rs.getInt(1);
            }

            //Insert Order Items + Update Stock
            for (OrderItem item : items) {

                // Insert into order_items
                try (PreparedStatement itemStmt =
                             conn.prepareStatement(orderItemSQL)) {

                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getPrice());

                    itemStmt.executeUpdate();
                }

                // Update stock
                try (PreparedStatement stockStmt =
                             conn.prepareStatement(updateStockSQL)) {

                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getProductId());

                    stockStmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewOrderHistory(int userId) {

        String sql = """
            SELECT o.id AS order_id,
                   o.order_date,
                   p.name AS product_name,
                   oi.quantity,
                   oi.price
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN products p ON oi.product_id = p.id
            WHERE o.user_id = ?
            ORDER BY o.order_date DESC
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            boolean hasOrders = false;

            while (rs.next()) {
                hasOrders = true;

                System.out.println(
                        "Order ID: " + rs.getInt("order_id") +
                                " | Date: " + rs.getTimestamp("order_date") +
                                " | Product: " + rs.getString("product_name") +
                                " | Qty: " + rs.getInt("quantity") +
                                " | Price: ₹" + rs.getBigDecimal("price")
                );
            }

            if (!hasOrders) {
                System.out.println("No orders found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTotalRevenue() {

        String sql = "SELECT SUM(total_amount) AS revenue FROM orders";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("Total Revenue: ₹"
                        + rs.getBigDecimal("revenue"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
