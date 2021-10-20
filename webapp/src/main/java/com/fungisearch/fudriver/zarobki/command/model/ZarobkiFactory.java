package com.fungisearch.fudriver.zarobki.command.model;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.repository.ZarobkiRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ZarobkiFactory {

    private final ZarobkiRepository zarobkiRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public ZarobkiFactory(ZarobkiRepository zarobkiRepository, BeanValidator beanValidator) {
        this.zarobkiRepository = zarobkiRepository;
        this.beanValidator = beanValidator;
    }

    public List<ZarobkiEntry> getWozek(Long wozekNr) {
        List<ZarobkiEntry> wozek = zarobkiRepository.findWozek(wozekNr);
        for (ZarobkiEntry entry : wozek) {
            entry.setZarobkiRepository(zarobkiRepository);
            entry.setBeanValidator(beanValidator);
        }
        return wozek;
    }

    public ZarobkiEntry getById(Long id) {
        ZarobkiEntry zarobkiEntry = zarobkiRepository.findById(id);
        zarobkiEntry.setBeanValidator(beanValidator);
        zarobkiEntry.setZarobkiRepository(zarobkiRepository);
        return zarobkiEntry;
    }

    public ZarobkiEntry.ZarobkiEntryBuilder builder() {
        return new ZarobkiEntry.ZarobkiEntryBuilder(this.zarobkiRepository, this.beanValidator);

    }

    public ZarobkiEntry getByPersonAndUniq(Long personId, Long uniqId) {
        ZarobkiEntry zarobkiEntry = zarobkiRepository.findByPersonAndUniq(personId, uniqId);
        if (zarobkiEntry != null) {
            zarobkiEntry.setBeanValidator(beanValidator);
            zarobkiEntry.setZarobkiRepository(zarobkiRepository);
        }
        return zarobkiEntry;
    }

    public Integer findMinDateForCycle(long cycleId){
        Date firstDate = zarobkiRepository.findMinDateForCycle(cycleId);
        return (firstDate != null ) ? Integer.parseInt(new DateTime(firstDate).toString("yyyyMMdd")): null;
    }

    public Integer findMaxDateForCycle(long cycleId){
        Date lastDate = zarobkiRepository.findMaxDateForCycle(cycleId);
        return (lastDate != null ) ? Integer.parseInt(new DateTime(lastDate).toString("yyyyMMdd")): null;
    }

    public List<ZarobkiEntry> findPersonZarobkiInMonth(long personId, long timeshort) {
        List<ZarobkiEntry> zarobki = zarobkiRepository.findPersonZarobkiInMonth(personId, timeshort);
        for(ZarobkiEntry zarobkiEntry: zarobki){
            zarobkiEntry.setZarobkiRepository(zarobkiRepository);
            zarobkiEntry.setBeanValidator(beanValidator);
        }
        return zarobki;
    }
}
