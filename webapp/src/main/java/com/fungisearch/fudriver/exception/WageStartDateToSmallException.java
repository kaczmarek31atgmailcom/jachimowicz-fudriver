package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 16.05.16.
 */
public class WageStartDateToSmallException extends RuntimeException {
    public WageStartDateToSmallException(String message){
        super(message);
    }
}
