package com.fungisearch.fudriver.reclassification.query.dao;

import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationDetailDto;
import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationHeaderDto;
import com.fungisearch.fudriver.reclassification.query.dto.ReclassificationPickerDto;
import com.fungisearch.fudriver.reclassification.query.dto.UnitDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public interface ReclassificationSkupDao {
    Set<Long> findRecliassificationSkupIds();
    List<LocalReclassificationHeaderDto> findProcessedLocalHeaders();
    List<LocalReclassificationHeaderDto> findNotProcessedLocalHeaders();
    LocalReclassificationHeaderDto findLocalHeader(Long id);
    List<LocalReclassificationDetailDto> getDetails(Long reclassificationId);
    UnitDto findSingleUnit(Long pickerId, Long uniqId);
}
