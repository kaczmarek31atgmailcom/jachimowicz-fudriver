package com.fungisearch.fudriver.common.command;

import org.apache.commons.httpclient.HttpMethod;

import java.io.IOException;




/**
 * Created by marcin on 04.02.16.
 */
public class ResponseEntity {
    public final int status;
    public final String body;

    ResponseEntity(HttpMethod method) throws IOException {
        this(method.getStatusCode(), method.getResponseBodyAsString());
    }

    ResponseEntity(ResponseEntity responseEntity) {
        this(responseEntity.status, responseEntity.body);
    }

    ResponseEntity(int status, String body) {
        this.status = status;
        this.body = body;
    }

}
