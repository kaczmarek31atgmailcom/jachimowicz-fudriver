package com.fungisearch.fudriver.cycle.query.dto;

import java.util.Date;

/**
 * Detale raportu zamkniętych cykli
 */
public class CycleTechnologistDetailDto {

    public Date date;
    public long technologistId;
    public String technologistName;
    public String technologistSurname;
    public long totalInne;
    public long totalKraj;
    public long totalExport;
    public long total;
    public long totalArea;
    public long totalWeight;
    public long quality;
    public long kgT;
    public long kgM;

    public long getTechnologistId() {
        return technologistId;
    }

    public Date getDate() {
        return date;
    }
}
