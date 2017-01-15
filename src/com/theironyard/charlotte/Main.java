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
                    String userEmail = session.attribute("userEmail");
                    String userName = session.attribute("userName");
//                    check for user in database
                    if (userId == null) {//if we don't have a User name lets ask them to log in
                        return new ModelAndView(m, "ScamAzon-login.html");
                    } else {//we have one...yay! lets make a view
                        User currentUser = User.selectUserById(conn, new User(userId));
                        m.put("name", currentUser.name );
                        m.put("email",currentUser.email);
                        m.put("orders", currentUser.orders);
                        return new ModelAndView(m, "scamAzonHome.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/ScamAzon-login",
                ((request, response) -> {
                    String userEmail = request.queryParams("loginEmail");
                    String userName = request.queryParams("loginName");
                    User reqU = new User(userEmail, userName);
                    User newUser = User.selectUserByNameAndEmail(conn, reqU);
                    if (newUser == null){
                        User.insertNewUser(conn, reqU);
                        newUser = User.selectUserByNameAndEmail(conn, reqU);
                        newUser.orders = new ArrayList<Item>();
                    }
                    else {
                        newUser.orders = User.innerJoinCart(conn, newUser.id);
                    }

                    Session session = request.session();
                    session.attribute("userId", newUser.id);
                    session.attribute("userName", newUser.name);
                    session.attribute("userEmail", newUser.email);
                    session.attribute("orders", newUser.orders);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/scamAzon-logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
//        Spark.get(
//                "/ScamAzon-Order",
//                ((request, response) -> {
//
//                })
//        );
    }
}
