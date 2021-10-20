package com.fungisearch.fudriver.type.command.model;

/**
 * Created by marcin on 17.03.16.
 */
public enum ExportType {
    EMPTY(0),
    KRAJ(1),
    EXPORT(2),
    INNE(3);

    public int index;
    ExportType(int index) {
        this.index = index;
    }
}
