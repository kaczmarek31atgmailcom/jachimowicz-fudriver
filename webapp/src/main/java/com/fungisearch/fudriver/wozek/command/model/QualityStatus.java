package com.fungisearch.fudriver.wozek.command.model;

/**
 * Created by marcin on 23.02.16.
 */
public enum QualityStatus {
    NOT_CHECKED (0),
    ACCEPTED (1),
    REJECTED (2);

    private final int index;

    QualityStatus(int index){
        this.index = index;
    }
    public int getIndex(){
        return this.index;
    }
}
