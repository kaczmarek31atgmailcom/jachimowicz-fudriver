package com.fungisearch.fudriver.zarobki.query.dao;

import com.fungisearch.fudriver.zarobki.query.dto.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ZarobkiDao {
    BoxBrcDto findBoxBrcByWarehouseId(Long boxId);
    List<BoxBrcDto> findReservedBoxesByUserId(Long userId);
    List<SummaryBrcDto> findBrcSummaryByUserId(Long userId);
    List<PickerZarobkiDto> getPickerZarobki(Date startDate, Date endDate);
    Map<Long,List<PickerZarobkiTypeGroupsDto>> getPickerZarobkiByGroups(Date startDate, Date endDate);
    Map<Long,Long> getPickerHours(Date startDate, Date endDate);
    List<ZarobkiByCycleDto> getZarobkiByCycle(long cycleId);
    List<DailyHarvestByTypeGroupDto> getDailyHarvestByGroupsForCycle(long cycleId);
    List<StandDetailDto> getStandDetails(long personId,Date startDate, Date endDate);
}
