package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationSkup;
import org.springframework.stereotype.Repository;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public interface ReclassificationSkupRepository {

    public Long save(ReclassificationSkup reclassificationSkup);

}
