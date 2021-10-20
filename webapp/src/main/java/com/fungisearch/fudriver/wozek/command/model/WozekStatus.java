package com.fungisearch.fudriver.wozek.command.model;

/**
 * Created by marcin on 23.02.16.
 */
public enum WozekStatus {
    ZEBRANY (0),
    NADANY_NUMER_WOZKA (1),
    CHLODNIA (2),
    ODRZUCONY (3),
    ZAPISANY (4),
    PRZYPISANY_DO_PALETY_MAGAZYNOWEJ (5);

    private final int index;

    WozekStatus(int index){
        this.index = index;
    }
}
