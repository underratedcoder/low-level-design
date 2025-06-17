package com.lld.router.service;

import com.lld.router.model.Response;

import java.util.Map;

//@FunctionalInterface
public interface Handler {
    Response handle(Map<String, String> pathParams);
}
