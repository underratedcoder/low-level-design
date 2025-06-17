package com.lld.router.service;// Router.java

import com.lld.router.enums.HttpMethod;
import com.lld.router.model.HttpRequest;
import com.lld.router.model.Response;
import com.lld.router.model.RouteTrieNode;

import java.util.*;

public class Router {
    private final RouteTrieNode root = new RouteTrieNode();

    public Router() { }

    public void addRoute(HttpMethod method, String path, Handler handler) {
        String[] parts = normalize(path);
        RouteTrieNode node = root;

        for (String part : parts) {
            if (part.startsWith(":")) {
                RouteTrieNode paramChild = node.children.get(":");
                if (paramChild == null) {
                    paramChild = new RouteTrieNode();
                    node.children.put(":", paramChild);
                }
                paramChild.paramName = part.substring(1);
                node = paramChild;
            } else if (part.startsWith("*")) {
                RouteTrieNode wildcardChildNode = new RouteTrieNode();
                node.wildcardChild = wildcardChildNode;
                node.wildcardChild.paramName = part.substring(1);
                node = wildcardChildNode;
                break; // wildcard matches the rest of the path
            } else {
                RouteTrieNode staticChild = node.children.get(part);
                if (staticChild == null) {
                    staticChild = new RouteTrieNode();
                    node.children.put(part, staticChild);
                }
                node = staticChild;
            }
        }

        node.handlers.put(method, handler);
    }

    public Response route(HttpRequest request) {
        Map<String, String> pathParams = new HashMap<>();
        RouteTrieNode node = root;
        String[] parts = normalize(request.getPath());

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (node.children.containsKey(part)) {
                node = node.children.get(part);
            } else if (node.children.containsKey(":")) {
                node = node.children.get(":");
                pathParams.put(node.paramName, part);
            } else if (node.wildcardChild != null) {
                node = node.wildcardChild;
                StringBuilder wildcardPath = new StringBuilder(part);
                for (int j = i + 1; j < parts.length; j++) {
                    wildcardPath.append("/").append(parts[j]);
                }
                pathParams.put(node.paramName, wildcardPath.toString());
                break;
            } else {
                return Response.notFound();
            }
        }

        Handler handler = node.handlers.get(request.getMethod());
        if (handler == null) {
            return node.handlers.isEmpty() ? Response.notFound() : Response.methodNotAllowed();
        }

        return handler.handle(pathParams);
    }

    private String[] normalize(String path) {
        return Arrays.stream(path.split("/"))
                     .filter(s -> !s.isEmpty())
                     .toArray(String[]::new);
    }
}
