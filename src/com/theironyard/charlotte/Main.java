package com.theironyard.charlotte;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
        Spark.staticFileLocation("/public");
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        User.createTable(conn);
        Order.createTable(conn);
        Item.createTable(conn);
        Spark.init();

//        Spark.get("/scamAzon",
//                (request, response) -> {
//                    return "";
//                });
//        Spark.get("/scamAzon-items",
//                (request, response) -> {
//            return "";
//                });Map sessions = new HashMap<>();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();//look up existing cookie value if there isn't one then we will set a new cookie value
                    //theses items are coming from the website to populate
                    Integer userId = session.attribute("userId");
                    Integer orderId = session.attribute("currentOrder");
//                    check for user in database
                    if (userId == null) {//if we don't have a User name lets ask them to log in
                        return new ModelAndView(m, "login.html");
                    } else {//we have one...yay! lets make a view
                        User currentUser = User.selectUserById(conn, new User(userId));
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
                        currentUser = User.insertAndReturnNewUser(conn, reqU);
                        //currentUser = User.selectUserByNameAndEmail(conn, reqU);
                        currentOrder = Order.insertAndReturnNewOrder(conn, currentUser);
//                        newUser.orders = new ArrayList<Item>();
                    }
                    else {
                        currentOrder = Order.returnLatestById(conn, reqU.id);
                    }
                    Session session = request.session();
                    session.attribute("userId", currentUser.id);
                    session.attribute("userName", currentUser.name);
                    session.attribute("currentOrder", currentOrder.id);
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
                    Integer userId = request.attribute("userId");
                    itemToCart.setName(request.queryParams("itemName"));
                    itemToCart.setQuantity(Integer.valueOf(request.queryParams("quantity")));
                    itemToCart.setPrice(Double.valueOf(request.queryParams("price")));
                    Item.updateItem(conn, itemToCart);
                    response.redirect("/restaurants");
                    return "";
                })
        );


    }
}
