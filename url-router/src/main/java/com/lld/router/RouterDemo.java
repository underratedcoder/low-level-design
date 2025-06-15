package com.lld.router;

import com.lld.router.enums.HttpMethod;
import com.lld.router.model.HttpRequest;
import com.lld.router.model.Response;
import com.lld.router.service.Router;

public class RouterDemo {
    public static void main(String[] args) {
        Router router = new Router();

        router.addRoute(HttpMethod.GET, "/home", p -> Response.ok("Home Page"));
        router.addRoute(HttpMethod.POST, "/users", p -> Response.ok("User Created"));
        router.addRoute(HttpMethod.GET, "/users/:userId", p -> Response.ok("User ID: " + p.get("userId")));
        router.addRoute(HttpMethod.GET, "/users/:userId/orders/:orderId",
                p -> Response.ok("User " + p.get("userId") + " Order " + p.get("orderId")));
        router.addRoute(HttpMethod.GET, "/static/*filePath",
                p -> Response.ok("Static file: " + p.get("filePath")));

        print(router.route(new HttpRequest(HttpMethod.GET, "/home"))); // 200
        print(router.route(new HttpRequest(HttpMethod.GET, "/users/123"))); // 200
        print(router.route(new HttpRequest(HttpMethod.GET, "/users/123/orders/456"))); // 200
        print(router.route(new HttpRequest(HttpMethod.GET, "/static/images/logo.png"))); // 200
        print(router.route(new HttpRequest(HttpMethod.GET, "/unknown"))); // 404
        print(router.route(new HttpRequest(HttpMethod.POST, "/users/123"))); // 405
    }

    private static void print(Response response) {
        System.out.println(response.getStatusCode() + ": " + response.getBody());
    }
}