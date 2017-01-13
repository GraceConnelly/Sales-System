package com.theironyard.charlotte;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class Item {
    Integer id;
    Integer userId;

    public Item() {
    }

    public Item(Integer userId) {
        this.userId = userId;
    }

    public Item(Integer id, Integer userId) {
        this.id = id;
        this.userId = userId;
    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS items (id IDENTITY, user_id INT)");
    }
}
