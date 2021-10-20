package com.fungisearch.fudriver.cycle.Exception;

/**
 * Created by marcin on 13.04.17.
 */
public class CycleStartDayAfterFirstHarvestDayException extends RuntimeException {

    private static final long serialVersionUID = 8100070415774574370L;
    private long cycleId;
    private int firstDate;
    private String message;

    public CycleStartDayAfterFirstHarvestDayException(long cycleId,String message, int firstDate) {
        super(message);
        this.cycleId = cycleId;
        this.message = message;
        this.firstDate = firstDate;
    }

    public int getFirstDate() {
        return firstDate;
    }

    public long getCycleId() {
        return cycleId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString(){
        return message;
    }
}
