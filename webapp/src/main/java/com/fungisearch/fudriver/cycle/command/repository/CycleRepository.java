package com.fungisearch.fudriver.cycle.command.repository;


import com.fungisearch.fudriver.cycle.command.model.Cycle;

public interface CycleRepository {
    void save(Cycle cycle);
    Cycle find(long id);
    Cycle findOpenCycleByChamberId(long chamberId);
}
