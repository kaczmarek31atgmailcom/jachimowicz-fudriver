package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 03.06.16.
 */
public class BatchFullyReservedException extends RuntimeException {
    public BatchFullyReservedException(){
        super("Ordered amount if fully reserved for this batch");
    }
}
