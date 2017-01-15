package com.theironyard.charlotte;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class Item {
    Integer id;
    String  name;
    Integer quantity;
    Double price;
    Integer orderId;

    public Item() {}

    public Item(String name, Integer quantity, Double price, Integer orderId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
    }

    public Item(Integer id, String name, Integer quantity, Double price, Integer orderId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS items (id IDENTITY, name VARCHAR, quantity INT, price DOUBLE, order_id INT)");
    }
    //populates a user based on selectMethods.
    public static Item populateItem(ResultSet results) throws SQLException {
        int id = results.getInt("id");
        String name = results.getString("name");
        int quantity = results.getInt("quantity");
        Double price = results.getDouble("price");
        int orderId = results.getInt("order_id");
        return new Item(id, name, quantity, price, orderId);
    }
}
