package com.fungisearch.fudriver.cycle.query.dto;

import com.fungisearch.fudriver.cycle.command.model.PeriodName;
import com.fungisearch.fudriver.zarobki.query.dto.DailyHarvestByTypeGroupDto;

import java.util.Date;
import java.util.List;

/**
 * Dane jednego rzutu dla pokazania szczegółów cyklu.
 */
public class CyclePeriodDto {
    public PeriodName periodName;
    public long totalKraj;
    public long totalInne;
    public long totalExport;
    public long total;
    public long quality;
    public long kgM;
    public long kgTon;
    public long kgDryTon;
    public List<CycleDayDto> cycleDates;
    public List<DailyHarvestByTypeGroupDto> typeGroups;
}
