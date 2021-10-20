package com.fungisearch.fudriver.reclassification.estimation.query.dto;

import java.util.Date;
import java.util.List;

public class CycleEstimationHeaderDto {
    public Long cycleId;
    public Integer squareMeters;
    public Date initDate;
    public List<CycleEstimationDetailDto> details;
}
