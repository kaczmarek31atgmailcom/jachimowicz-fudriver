package com.fungisearch.fudriver.reclassification.estimation.command.repository;

import com.fungisearch.fudriver.reclassification.estimation.command.model.HarvestEstimation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class HarvestEstimationRepositoryImpl implements HarvestEstimationRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(HarvestEstimation harvestEstimation) {
        em.persist(harvestEstimation);
    }
}
