package com.fungisearch.fudriver.cycle.Exception;

/**
 * Created by marcin on 13.04.17.
 */
public class CycleStartDateNotBeforeOtherCycleDatesException extends RuntimeException {

    @Override
    public String toString(){
        return "Cycle start date should be the first date in the cycle.";
    }
}
