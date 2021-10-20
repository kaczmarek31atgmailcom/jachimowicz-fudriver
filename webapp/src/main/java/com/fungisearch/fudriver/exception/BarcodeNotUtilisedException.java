package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 23.02.16.
 */
public class BarcodeNotUtilisedException extends RuntimeException {
    public BarcodeNotUtilisedException(Long pickerId, Long uniqId) {
        super("Kod " + uniqId + " dla zbieraczki " + pickerId + " nie zostal zuzyty");
    }
}
