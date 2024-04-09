package service;
import Delivery.*;
import exception.InvalidDataException;
import orderInfo.*;

import java.util.*;
public class ProductService {
    private static final List<Product> productList = new ArrayList<>();
    private static int nextProductId = 1;

    public static List<Product> getProductList() {
        return productList;
    }

    protected static Product getProductById(int productId) {
        for (Product product : productList) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public static void createProduct(String name, double price) {
        /// it is impossible that the price is less than 0 :)
        try {
            if (price < 0) {
                throw new InvalidDataException("Product price cannot be negative.");
            }
            productList.add(new Product(nextProductId++, name, price));
        } catch (InvalidDataException e) {
            System.out.println("Error creating product: " + e.getMessage());
        }

    }
}