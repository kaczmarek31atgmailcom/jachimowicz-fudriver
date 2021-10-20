package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.model.WozekEntry;
import com.fungisearch.fudriver.wozek.command.repository.WozekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by marcin on 21.04.16.
 */
@Service
public class RejectWozekImpl implements RejectWozek {

    @Autowired
    private WozekRepository wozekRepository;

    @Autowired
    private BeanValidator beanValidator;

    @Override
    public void reject(Long wozekNr) {
        List<WozekEntry> wozekEntries = wozekRepository.getEntriesForWozekId(wozekNr);
        for(WozekEntry entry : wozekEntries){
            entry.setBeanValidator(beanValidator);
            entry.setWozekRepository(wozekRepository);
            entry.reject();
        }
    }
}
