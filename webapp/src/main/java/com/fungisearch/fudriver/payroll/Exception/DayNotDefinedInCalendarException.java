package com.fungisearch.fudriver.payroll.Exception;

/**
 * Created by marcin on 14.06.16.
 */
public class DayNotDefinedInCalendarException extends RuntimeException {
    public DayNotDefinedInCalendarException(){
        super("Dzień nie został zdefiniowany w kalendarzu");
    }
}
