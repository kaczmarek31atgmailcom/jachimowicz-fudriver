package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 03.03.16.
 */
public class NonExistingTimeSheetUpdateException extends RuntimeException {
    public NonExistingTimeSheetUpdateException(){
        super("Próba modyfikacji nieistniejącego timesheeta");
    }
}
