package com.fungisearch.fudriver.type.query.dao;

import com.fungisearch.fudriver.type.query.dto.*;

import java.util.Date;
import java.util.List;

/**
 * Created by idea on 09.03.16.
 */
public interface TypeDao {
    List<WozekTypeDto> findTypesForWozek();
    List<MassHarvestTypeDto> findTypesForMassHarvest();
    List<OrderTypeDto> findOrderTypes();
    Long findExportId(Long id);
    Double findWeight(Long id);
    List<TypeGroupDto> getActiveTypeGroups();
    List<TypeDto> getActiveTypes();
    List<TypeSizeDto> getActiveTypeSizes();
    List<TypeSizeInCyclesDto> getTypeSizesInCycles(Date startDate, Date endDate);
}
