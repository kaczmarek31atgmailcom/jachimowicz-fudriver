package com.fungisearch.fudriver.cycle.query.dao;

import com.fungisearch.fudriver.cycle.query.dto.*;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.TechnoCycleDetail;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.TechnoCycleHeader;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface CycleDao {
    List<WozekCycleDto> findCyclesForWozek();
    List<MassHarvestCycleDto> findCycleForMassHarvest();
    Set<CycleHeaderDto> findAllCycles(int startDay, int endDay);
    CycleHeaderDto findHeader(long cycleId);
    Set<CycleDatesDto> findCurrentCycleDates();
    CycleDatesDto findCycleDatesById(long id);
    CycleDatesDto findCycleDatesByChamberId(long id);
    List<CycleByBrigadeDto> findCycleByBrigades(Date startDate, Date endDate);
    List<TechnoCycleHeader> findTechnoHeaders(int startDate, int endDate);
    List<TechnoCycleDetail> findTechnoDetails(int startDate, int endDate);
}
