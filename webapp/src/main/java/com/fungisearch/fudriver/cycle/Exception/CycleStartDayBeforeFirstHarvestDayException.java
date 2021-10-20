package com.fungisearch.fudriver.cycle.Exception;

/**
 * Created by marcin on 22.04.17.
 */
public class CycleStartDayBeforeFirstHarvestDayException extends RuntimeException {

    @Override
    public String toString(){
        return "Cycle start day set not after last harvest day";
    }
}
