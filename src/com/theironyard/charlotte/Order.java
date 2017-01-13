package com.theironyard.charlotte;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class Order {
    Integer id;
    String  name;
    Integer quantity;
    Double price;
    Integer itemId;

    public Order() {}

    public Order(String name, Integer quantity, Double price, Integer orderId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.itemId = orderId;
    }

    public Order(Integer id, String name, Integer quantity, Double price, Integer orderId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.itemId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS orders (id IDENTITY, name VARCHAR, quantity INT, price DOUBLE, order_id INT)");
    }
}
