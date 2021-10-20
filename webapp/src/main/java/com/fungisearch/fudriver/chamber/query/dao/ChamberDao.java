package com.fungisearch.fudriver.chamber.query.dao;

import com.fungisearch.fudriver.chamber.query.dto.ChamberTypesDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 17.03.16.
 */
public interface ChamberDao {
    Map<Long, Long> getCycleDepotMapping();
    List<ChamberTypesDto> getHarvestByChamber(Date startDate, Date endDate);
}
