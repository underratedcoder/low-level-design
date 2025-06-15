package com.lld.router.model;// Response.java

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response {
    private final int statusCode;
    private final String body;

    public static Response notFound() {
        return new Response(404, "404 Not Found");
    }

    public static Response methodNotAllowed() {
        return new Response(405, "405 Method Not Allowed");
    }

    public static Response ok(String body) {
        return new Response(200, body);
    }
}
