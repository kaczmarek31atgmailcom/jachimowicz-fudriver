package com.fungisearch.fudriver.cycle.query.dto;

import java.util.List;

/**
 * Header dla raportu wyników technologów dla cykli zamkniętych w danym okresie.
 */
public class CycleTechnologistHeaderDto {
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
    public long cyclesAmount;

    public long getTechnologistId() {
        return technologistId;
    }
}
