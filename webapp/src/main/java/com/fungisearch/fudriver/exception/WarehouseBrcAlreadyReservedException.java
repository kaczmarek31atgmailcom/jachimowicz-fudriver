package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 22.04.16.
 */
public class WarehouseBrcAlreadyReservedException extends RuntimeException {
    public WarehouseBrcAlreadyReservedException(){
        super("Can not reserve box that is already reserved");
    }
}
