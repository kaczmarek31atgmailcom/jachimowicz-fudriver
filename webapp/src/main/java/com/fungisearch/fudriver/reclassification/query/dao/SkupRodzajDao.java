package com.fungisearch.fudriver.reclassification.query.dao;

import com.fungisearch.fudriver.reclassification.query.dto.LocalRodzajDto;
import com.fungisearch.fudriver.reclassification.query.dto.SkupRodzajDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public interface SkupRodzajDao {
    List<SkupRodzajDto> getAllTypes();
    List<LocalRodzajDto> getAllLocalTypes();
    List<LocalRodzajDto> getActiveLocalTypes();
    Map<Long,LocalRodzajDto> getMapOfAllLocalTypes();
}
