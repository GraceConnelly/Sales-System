package com.theironyard.charlotte;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
        Spark.staticFileLocation("/public");
       // Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        User.createTable(conn);
        Order.createOrderTable(conn);
        Order.createOrderItemsTable(conn);
        Item.createTable(conn);
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();
                    User currentUser = User.selectUserById(conn, session.attribute("userId"));
                    Order currentOrder = Order.selectOpenOrdersByID(conn, session.attribute("orderId"));
                    //check for user in database
                    if (currentUser == null) {
                        //if we don't have a User name lets ask them to log in
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        //we have a user
                        if (currentOrder == null){
                            currentOrder = Order.insertAndReturnNewOrder(conn, currentUser);
                            session.attribute("orderId", currentOrder.id);
                        }
                        m.put("name", currentUser.name );
                        m.put("email",currentUser.email);
                        m.put("inventory",Item.listAllItems(conn));
                        return new ModelAndView(m, "Home.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/login",
                ((request, response) -> {
                    String userEmail = request.queryParams("loginEmail");
                    String userName = request.queryParams("loginName");
                    User reqU = new User(userEmail, userName);
                    User currentUser = User.selectUserByNameAndEmail(conn, reqU);
                    Order currentOrder;
                    if (currentUser == null){
                        //if there is no current user in session lets make one.
                        currentUser = User.insertAndReturnNewUser(conn, reqU);
                        currentOrder = Order.insertAndReturnNewOrder(conn, currentUser);
                    }
                    else {
                        //if there is user lets check for and get the most recent order.
                        // if there isnt an open order lets just create one.
                        currentOrder = Order.returnOrCreateLatestById(conn, currentUser);
                    }
                    Session session = request.session();
                    session.attribute("userId", currentUser.id);
                    session.attribute("orderId", currentOrder.id);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/add-to-cart",
                ((request, response) -> {
                    Item itemToCart = new Item();
                    Session session = request.session();
                    User currentUser = User.selectUserById(conn, session.attribute("userId"));
                    Order currentOrder = Order.selectOpenOrdersByID(conn, session.attribute("orderId"));
                    Integer itemId = Integer.valueOf(request.queryParams("id"));
                    //if any of our required values turn out to be invalid lets go home.
                    if (request.queryParams("quantity").equals("") || currentUser == null || currentOrder == null) {
                        response.redirect("/");
                        return "";
                    }
                    Integer qty = Integer.valueOf(request.queryParams("quantity"));
                    if (itemId == null) {
                        itemToCart.setName(request.queryParams("itemName"));
                        itemToCart.setPrice(Double.valueOf(request.queryParams("price")));
                        itemToCart.setQuantity(qty);
                        //if item exists get its id
                        //if item doesn't exist then I want to add it to my Items Table
                        itemToCart = Item.insertSelectItemByNameAndPrice(conn, itemToCart);
                    }
                    else{
                        itemToCart = Item.insertSelectItemById(conn, itemId);
                        itemToCart.setQuantity(qty);
                    }
                    //when item exists check if item is in orderItems. if not add
                    //if it does then update quantity.
                    Order.insertUpdateOrderItems(conn, itemToCart, currentOrder.id);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.get(
                "/cart",
                ((request, response) ->{
                    HashMap m = new HashMap();
                    Session session = request.session();
                    User currentUser = User.selectUserById(conn, session.attribute("userId"));
                    Order currentOrder = Order.selectOpenOrdersByID(conn, session.attribute("orderId"));
                    if (currentUser == null || currentOrder == null) {
                        response.redirect("/");
                        return new ModelAndView(m, "login.html");
                    }
                        m.put("name", currentUser.name );
                        currentOrder.items = Order.innerJoinItems(conn, currentOrder.id);
                        m.put("cart",currentOrder.items);
                        m.put("total",Order.calcTotals(conn, currentOrder.items));
                        return new ModelAndView(m, "cart.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/checkout",
                (((request, response) -> {
                    Session session = request.session();
                    Order.checkout(conn, session.attribute("orderId"));
                    response.redirect("/");
                    return "";
                }))
        );

        Spark.get(
                "/orders",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();
                    User currentUser = User.selectUserById(conn, session.attribute("userId"));
                    m.put("name", currentUser.name );
                    m.put("orders",User.getAllUserOrders(conn, currentUser.id));
                    return new ModelAndView(m,"orders.html");
                }),
                new MustacheTemplateEngine()
        );
    }
}