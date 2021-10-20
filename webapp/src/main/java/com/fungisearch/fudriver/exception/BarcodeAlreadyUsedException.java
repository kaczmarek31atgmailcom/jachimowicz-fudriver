package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 23.02.16.
 */
public class BarcodeAlreadyUsedException extends RuntimeException {
    public BarcodeAlreadyUsedException(Long pickerId, Long uniqId){
        super("Kod " + uniqId + " dla zbieraczki " + pickerId + " juz zostal zuzyty");
    }
}
