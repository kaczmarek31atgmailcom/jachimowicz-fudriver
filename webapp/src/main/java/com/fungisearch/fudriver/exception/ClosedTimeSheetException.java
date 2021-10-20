package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 03.03.16.





 */
public class ClosedTimeSheetException extends RuntimeException {
    public ClosedTimeSheetException(){
        super("Próba zamknięcia już zamkniętego timesheetu");
    }
}
