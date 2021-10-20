package com.fungisearch.fudriver.zarobki.command.repository;

import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by idea on 14.03.16.
 */
public interface ZarobkiRepository {
    Long save(ZarobkiEntry zarobkiEntry);
    List<ZarobkiEntry> findWozek(Long wozekNr);
    void delete(ZarobkiEntry zarobkiEntry);
    ZarobkiEntry findById(Long id);
    ZarobkiEntry findByPersonAndUniq(Long personId, Long uniqId);
    Date findMinDateForCycle(long cycleId);
    Date findMaxDateForCycle(long cycleId);

    List<ZarobkiEntry> findPersonZarobkiInMonth(long personId, long timeshort);
    List<Long> getLudzieIdsInMonth(long timesort);
}
