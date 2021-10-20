package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 06.04.16.
 */
public class RfidNotExistsException extends RuntimeException {
    public RfidNotExistsException(String rfid){
        super("Podany klucz rfid " + rfid + " nie istnieje w bazie");
    }
}
