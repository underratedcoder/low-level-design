package com.lld.router.model;// HttpRequest.java

import com.lld.router.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequest {
    private final HttpMethod method;
    private final String path;
}
