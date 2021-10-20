package com.fungisearch.fudriver.reclassification.estimation.command.repository;

import com.fungisearch.fudriver.reclassification.estimation.command.model.HarvestEstimation;

public interface HarvestEstimationRepository {
    void save(HarvestEstimation harvestEstimation);
}
