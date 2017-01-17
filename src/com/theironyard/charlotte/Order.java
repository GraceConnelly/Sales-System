package com.theironyard.charlotte;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class Order {
    Integer id;
    Integer userId;
    boolean open;
    ArrayList<Item> items;

    public Order() {
    }

    public Order(Integer userId) {
        this.userId = userId;
    }

    public Order(Integer userId, boolean open) {
        this.userId = userId;
        this.open = open;
    }

    public Order(Integer id, Integer userId, boolean open) {
        this.id = id;
        this.userId = userId;
        this.open = open;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public static void createOrderTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS orders (id IDENTITY, user_id INT, open BOOLEAN)");
    }

    public static void createOrderItemsTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS orders (id IDENTITY, user_id INT, open BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS order_items ( "+
                        "order_id INT NOT NULL, " +
                        "item_id INT NOT NULL, " +
                        "quantity INT NOT NULL," +
                "CONSTRAINT pk_OrderItem PRIMARY KEY (order_id, item_id))");
        }

    //Populates order object with returned data from database
    public static Order populateOrder(ResultSet results) throws SQLException {
            int id = results.getInt("id");
            int userId = results.getInt("user_id");
            boolean open = results.getBoolean("open");
            return new Order(id, userId, open);
    }
    //START Pulls Things from Database
    public static Order returnLatestById (Connection conn, User currentU) throws SQLException{
        if (currentU.id != null) {
            PreparedStatement stmt = conn.prepareStatement("SELECT TOP 1 * FROM orders where user_id = ? and open = true Order by id DESC");
            stmt.setInt(1, currentU.id);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return populateOrder(results);
            } else {
                return insertAndReturnNewOrder(conn, currentU);
            }
        }
        return null;
    }

    public static Order selectOrdersByID (Connection conn, Integer id) throws SQLException {
        if (id != null) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders where id = ?");
            stmt.setInt(1, id);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return populateOrder(results);
            }
        }
        return null;
    }

    //START Methods that Alter Database

    public static void insertUpdateOrderItems(Connection conn, Item item, int orderId ) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT TOP 1 * FROM order_items where order_id = ? and item_id =?");
        stmt.setInt(1, orderId);
        stmt.setInt(2, item.id);
        ResultSet results = stmt.executeQuery();
        if(results.next()) {
            orderItemUpdate(conn, item, orderId);
        }
        else {
            insertOrderItem(conn, item, orderId);
        }
    }

    public static Order insertAndReturnNewOrder (Connection conn, User currentU) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders values (NULL, ?, ?)");
                stmt.setInt(1, currentU.id);
                stmt.setBoolean(2, true);
                stmt.execute();
        return returnLatestById(conn, currentU);
    }
    public static void orderItemUpdate(Connection conn, Item item, int orderId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("UPDATE order_items SET quantity = quantity + ? WHERE order_id = ? AND item_id = ?");
                stmt.setInt(1, item.quantity);
                stmt.setInt(2, orderId);
                stmt.setInt(3, item.id);
                stmt.execute();
    }
    public static void insertOrderItem(Connection conn, Item item, int orderId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO order_items values (? , ?, ?)");
                stmt.setInt(1, orderId);
                stmt.setInt(2, item.id);
                stmt.setInt( 3, item.quantity);
                stmt.execute();
    }

    public static ArrayList<Item> innerJoinItems(Connection conn, int id) throws SQLException {
        ArrayList<Item> item = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("Select order_items.item_id, items.name, items.price, order_items.quantity, order_items.order_id  from items\n" +
                "INNER JOIN order_items ON items.id = Order_items.item_id WHERE order_items.order_id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            item.add(Item.populatePurchaseItem(results));
        }
        return item;
    }
    //public static c
}
