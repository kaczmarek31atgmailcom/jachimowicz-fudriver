package com.fungisearch.fudriver.cycle.Exception;

/**
 * Created by marcin on 13.04.17.
 */
public class CycleSecondPeriodAfterStartThirdPeriodDateException extends RuntimeException {

    @Override
    public String toString(){
        return "Second period start time after the third period start time";
    }
}
