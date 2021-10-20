package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Chamber;

/**
 * Created by marcin on 08.04.17.
 */
public interface ChamberRepository {
    void save(Chamber chamber);
    Chamber find(long id);
}
