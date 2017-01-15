package com.theironyard.charlotte;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class Order {
    Integer id;
    Integer userId;
    Integer open;
    ArrayList<Item> items;

    public Order() {
    }

    public Order(Integer userId) {
        this.userId = userId;
    }

    public Order(Integer id, Integer userId) {
        this.id = id;
        this.userId = userId;
    }

    public Order(Integer id, Integer userId, Integer open) {
        this.id = id;
        this.userId = userId;
        this.open = open;
    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS orders (id IDENTITY, user_id INT)");
    }
}
