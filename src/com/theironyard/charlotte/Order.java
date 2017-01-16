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

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS orders (id IDENTITY, user_id INT, open BOOLEAN)");
    }

        public static Order populateOrder(ResultSet results) throws SQLException {
            int id = results.getInt("id");
            int userId = results.getInt("user_id");
            boolean open = results.getBoolean("open");
            return new Order(id, userId, open);
        }

    public static Order returnLatestById (Connection conn, Integer userId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders where user_id = ? Order by id DESC");
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        if(results.next()) {
            return populateOrder(results);
        }
        else {
            return null;
        }
    }
    public static Order insertAndReturnNewOrder (Connection conn, User currentU) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders values (NULL, ?, ?)");
                stmt.setInt(1, currentU.id);
                stmt.setBoolean(2, false);
                stmt.execute();
        return returnLatestById(conn, currentU.id);

    }
    public static ArrayList<Item> innerJoinItems(Connection conn, int id) throws SQLException {
        ArrayList<Item> items = null;
        PreparedStatement stmt = conn.prepareStatement("Inner join items on item.order_id = orders.id;");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            items.add(Item.populateItem(results));
        }
        return items;
    }
}
