package com.fungisearch.fudriver.reclassification.estimation.query.dto;

import java.util.Date;

public class CycleEstimationDetailDto {
    public Date day;
    public Integer groupId;
    public Integer weight;

    @Override
    public String toString() {
        return "CycleEstimationDetailDto{" +
                "day=" + day +
                ", groupId=" + groupId +
                ", weight=" + weight +
                '}';
    }
}
