package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by graceconnelly on 1/12/17.
 */
public class User {
    Integer id;
    String email;
    String name;
    ArrayList<Item> orders;

    public User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Item> orders) {
        this.orders = orders;
    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, email VARCHAR, name VARCHAR)");
    }

    //populates a user based on selectMethods.
    public static User populateUser(ResultSet results) throws SQLException {
        int id = results.getInt("id");
        String email = results.getString("email");
        String name = results.getString("name");
        return new User(id, email, name);
    }
    //Gets info from database
    public static User selectUserByNameAndEmail(Connection conn, User reqUser) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where upper(email) = ? and upper(name) = ?");
        stmt.setString(1, reqUser.getEmail().toUpperCase());
        stmt.setString(2, reqUser.getName().toUpperCase());
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            return populateUser(results);
        } else {
            return null;
        }
    }

    public static User selectUserById(Connection conn, User currentUser) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where id = ?");
        stmt.setInt(1, currentUser.id);
        ResultSet results = stmt.executeQuery();
        if(results.next()) {
            return populateUser(results);
        }
        else {
            return null;
        }
    }

    public static ArrayList<Item> innerJoinCart(Connection conn, int id) throws SQLException{
        ArrayList<Item> items = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders where user_id = ?;" +
                "Inner join items on item.order_id = orders.id;");
        stmt.setInt(1,id);
        ResultSet results = stmt.executeQuery();
        while (results.next()){
            items.add(Item.populateItem(results));
        }
        return items;
    }
    //Alters the Database
    public static void insertNewUser(Connection conn, User newUser) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, newUser.getEmail());
        stmt.setString(2, newUser.getName());
        stmt.execute();
    }
}
