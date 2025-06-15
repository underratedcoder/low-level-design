package com.lld.router.model;// RouteTrieNode.java
import com.lld.router.enums.HttpMethod;
import com.lld.router.service.Handler;

import java.util.*;


public class RouteTrieNode {
    public Map<String, RouteTrieNode> children = new HashMap<>();
    public Map<HttpMethod, Handler> handlers = new HashMap<>();
    public String paramName = null;
    public RouteTrieNode wildcardChild = null;
}
