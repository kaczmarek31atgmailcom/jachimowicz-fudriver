package com.fungisearch.fudriver.cycle.Exception;


public class CycleSecondPeriodBeforeStartDateException extends RuntimeException{

    @Override
    public String toString() {
        return "Start date of cycle second period before cycle start date";
    }
}
