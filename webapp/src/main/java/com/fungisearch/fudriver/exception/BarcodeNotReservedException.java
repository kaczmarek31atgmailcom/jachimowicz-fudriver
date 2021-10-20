package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 23.02.16.
 */
public class BarcodeNotReservedException extends RuntimeException {
    public BarcodeNotReservedException(Long pickerId, Long uniqId){
        super("Kod " + uniqId + " dla zbieraczki id " + pickerId + " nie zostal zarezerwowany");
    }
}
