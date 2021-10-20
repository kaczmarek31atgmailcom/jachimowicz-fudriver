package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Depot;

/**
 * Created by marcin on 08.04.17.
 */
public interface DepotRepository {
    void save(Depot depot);

    Depot find(long id);
}
