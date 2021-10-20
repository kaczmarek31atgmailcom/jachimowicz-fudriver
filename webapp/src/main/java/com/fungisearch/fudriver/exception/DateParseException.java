package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 13.05.16.
 */
public class DateParseException extends RuntimeException {
    public DateParseException(){
        super("Bład parsowania daty");
    }
}
