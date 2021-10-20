package com.fungisearch.fudriver.cycle.query.dto;

import java.util.List;

/**
 * Grupuje kolekcję headerów i detali dla raportu zamkniętych cykli do oceny pracy technologów
 */
public class CycleTechnologistDto {
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
    public List<CycleTechnologistHeaderDto> headers;
    public List<CycleTechnologistDetailDto> details;
}
