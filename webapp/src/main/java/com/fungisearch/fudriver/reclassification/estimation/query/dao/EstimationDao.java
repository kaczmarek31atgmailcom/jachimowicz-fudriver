package com.fungisearch.fudriver.reclassification.estimation.query.dao;

import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationHeaderDto;

import java.util.List;

public interface EstimationDao  {
    List<CycleEstimationHeaderDto> getAllCycles();
}
