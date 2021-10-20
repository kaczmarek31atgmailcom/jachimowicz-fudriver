package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 03.03.16.
 */
public class NotClosedTimeSheetException extends RuntimeException {
    public NotClosedTimeSheetException(){
        super("Nie mogę otworzyć nowego timesheeta gdy poprzedni nie jest zamknięty");
    }
}
