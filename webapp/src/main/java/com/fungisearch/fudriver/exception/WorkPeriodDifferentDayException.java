package com.fungisearch.fudriver.exception;

public class WorkPeriodDifferentDayException extends RuntimeException {
    public WorkPeriodDifferentDayException() {
        super("Okres pracy musi rozpocząć się i zakończyć tego samego dnia");
    }
}
