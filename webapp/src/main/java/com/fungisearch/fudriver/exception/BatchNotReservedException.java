package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 06.06.16.
 */
public class BatchNotReservedException extends RuntimeException {
    public BatchNotReservedException(){
        super("Can not release any item while non is reserved");
    }
}
