package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 24.04.16.
 */
public class WarehouseBrcNotReservedException extends RuntimeException {
    public WarehouseBrcNotReservedException(){
        super("Can not release not reserved box");
    }
}
