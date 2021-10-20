package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import com.fungisearch.fudriver.reclassification.command.model.ReclassificationSkup;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by marcin on 17.02.16.
 */
@Repository
public interface ReclassificationRepository {

    List<ReclassificationDetailSkup> getReclassificationDetails(Long id);
    List<ReclassificationDetailSkup> getActiveReclassificationDetails(Long id);
    ZarobkiEntry findZarobki(Long pickerId, Long uniqId);
    void updateZarobkiEntry(ZarobkiEntry zarobkiEntry);
    ReclassificationSkup findReclassification(Long id);
    void updateReclassificationSkup(ReclassificationSkup reclassificationSkup);
}
