package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import org.springframework.stereotype.Repository;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public interface ReclassificationDetailSkupRepository {
    void save(ReclassificationDetailSkup detail);
    ReclassificationDetailSkup find(Long id);
    void update(ReclassificationDetailSkup detailSkup);

}
