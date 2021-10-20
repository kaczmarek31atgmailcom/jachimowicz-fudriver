package com.fungisearch.fudriver.cycle.Exception;

/**
 * Created by marcin on 13.04.17.
 */
public class CycleSecondPeriodAfterCycleEndDateException extends RuntimeException {

    @Override
    public String toString(){
        return "Cycle start second period should not be after start third period or after cycle end date.";
    }
}
