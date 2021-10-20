package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 04.04.16.
 */
public class RfidDuplicationException extends RuntimeException {
    public RfidDuplicationException(){
        super("Klucz RFID jest już używany");
    }
}
