package service;

import dao.ProductDAO;
import model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public boolean addProduct(String name, BigDecimal price, int stock) {

        if (name == null || name.isBlank()) {
            System.out.println("Product name cannot be empty.");
            return false;
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Price must be greater than zero.");
            return false;
        }

        if (stock < 0) {
            System.out.println("Stock cannot be negative.");
            return false;
        }

        Product product = new Product(name, price, stock);
        return productDAO.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product findProductById(int id) {
        return productDAO.findById(id);
    }
}
