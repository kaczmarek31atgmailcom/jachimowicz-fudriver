package com.fungisearch.fudriver.cycle.query.dto;

import java.util.Date;

/**
 * Dane jednego dnia dla pokazania szczegółów cyklu.
 */
public class CycleDayDto {
    public Date date;
    public long summaryKraj;
    public long summaryInne;
    public long summaryExport;
    public long summaryTotal;
    public long quality;
    public long kgM;
    public long kgTon;
    public long kgDryTon;

    public Date getDate() {
        return date;
    }

    public long getSummaryKraj() {
        return summaryKraj;
    }

    public long getSummaryInne() {
        return summaryInne;
    }

    public long getSummaryExport() {
        return summaryExport;
    }

    public long getSummaryTotal() {
        return summaryTotal;
    }

    public long getQuality() {
        return quality;
    }

    public long getKgM() {
        return kgM;
    }

    public long getKgTon() {
        return kgTon;
    }

    public long getKgDryTon() {
        return kgDryTon;
    }
}
