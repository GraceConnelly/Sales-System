package com.theironyard.charlotte;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class Item {
    Integer id;
    String  name;
    Double price;
    Integer quantity;
    Integer orderId;

    public Item() {
    }

    public Item(String name, Double price, Integer quantity, Integer orderId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public Item(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Item(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Item(Integer id, String name, Double price, Integer quantity, Integer orderId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS items (id IDENTITY, name VARCHAR, price DOUBLE)");
    }
    //populates a user based on selectMethods.
    public static Item populateItem(ResultSet results) throws SQLException {
        int id = results.getInt("id");
        String name = results.getString("name");
        Double price = results.getDouble("price");
        return new Item(id, name, price);
    }
    public static Item populatePurchaseItem(ResultSet results) throws SQLException {
        int id = results.getInt("item_id");
        String name = results.getString("name");
        Double price = results.getDouble("price");
        Integer quantity = results.getInt("quantity");
        Integer orderId = results.getInt("order_id");
        return new Item(id, name, price, quantity, orderId);
    }

    public static ArrayList<Item> listAllItems(Connection conn) throws SQLException {
        ArrayList<Item> inventory = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM items");
        while (results.next()) {
            inventory.add(populateItem(results));
        }
        return inventory;
    }

    public static Item insertSelectItemByNameAndPrice(Connection conn, Item item) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT TOP 1 * FROM items where upper(name) = ? and price = ?");
        stmt.setString(1, item.name.toUpperCase());
        stmt.setDouble(2, item.price);
        ResultSet results = stmt.executeQuery();
        if(results.next()) {
            return populateItem(results);
        }
        else {
            return insertItem(conn, item);
        }
    }

    public static Item insertItem(Connection conn, Item newItem) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items values (NULL, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, newItem.name);
        stmt.setDouble(2, newItem.price);
        stmt.executeUpdate();
        ResultSet item = stmt.getGeneratedKeys();
        item.next();
        newItem.setId(item.getInt(1));
        return newItem;

    }

    public static void updateItem (Connection conn, Item item) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES (NULL, ?, ?, ?, ?)");
        stmt.setString(1, item.getName());
        stmt.setInt(2, item.getQuantity());
        stmt.setDouble(3, item.getPrice());
        stmt.setInt(4, item.getOrderId());
    }
//
//    public static boolean lookUpItem (Connection conn, Item item, Order order) throws SQLException {
//        PreparedStatement stmt = conn.prepareStatement("");
//    }
}
