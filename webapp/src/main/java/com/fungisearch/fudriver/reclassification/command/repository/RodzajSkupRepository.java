package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupGrupa;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkup;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public interface RodzajSkupRepository {


    RodzajSkup get(Long id);
    List<RodzajSkup> getAllRodzajSkup();
    void save(RodzajSkup rodzajSkup);
    void save(RodzajSkupGrupa rodzajSkupGrupa);

    RodzajSkupGrupa findByGroupIdRodzajSkupGrupa(long groupId);

    void remove(RodzajSkup rodzajSkup);

}
