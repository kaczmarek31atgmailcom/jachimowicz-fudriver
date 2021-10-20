package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.WozekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by marcin on 23.02.16.
 */
@Service
public class WozekEntryFactory {

    @Autowired
    WozekRepository wozekRepository;

    @Autowired
    BeanValidator beanValidator;

    @Autowired
    UniqFactory uniqFactory;


    public WozekEntry.WozekEntryBuilder builder() {
        WozekEntry.WozekEntryBuilder wozekEntryBuilder = new WozekEntry.WozekEntryBuilder(wozekRepository, beanValidator);
        return wozekEntryBuilder;
    }

    public WozekEntry get(Long id) {
        WozekEntry wozekEntry = wozekRepository.findOne(id);
        wozekEntry.setBeanValidator(beanValidator);
        wozekEntry.setWozekRepository(wozekRepository);
        return wozekEntry;
    }

    public List<WozekEntry> getWozekEntriesOfPalleteId(Long paletteId) {
        List<WozekEntry> wozek = wozekRepository.getEntriesForWozekId(paletteId);
        for (WozekEntry wozekEntry : wozek) {
            wozekEntry.setBeanValidator(this.beanValidator);
            wozekEntry.setWozekRepository(this.wozekRepository);
        }
        return wozek;
    }

    public boolean isProposedWozekAmountValid(Long amount, Long nr) {
        return amount.equals(wozekRepository.getTotalAmount(nr));
    }

    public WozekEntry findByPickerAndUniq(Long pickerId, Long uniqId) {
        WozekEntry wozekEntry = wozekRepository.findByPickerAndUniq(pickerId, uniqId);
        if (wozekEntry != null) {
            wozekEntry.setWozekRepository(wozekRepository);
            wozekEntry.setBeanValidator(beanValidator);
        }
        return wozekEntry;
    }
}
