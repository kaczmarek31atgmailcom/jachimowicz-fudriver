package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 10.06.16.
 */
public class StartDateAfterEndDateException extends RuntimeException {
    public StartDateAfterEndDateException(){
        super("Start date can not be after end date");
    }
}
