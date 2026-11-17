package com.fungisearch.fudriver.exception;

public class OverlappingWorkPeriodException extends RuntimeException {
    public OverlappingWorkPeriodException() {
        super("Okres pracy nakłada się na inny okres tego pracownika");
    }
}
