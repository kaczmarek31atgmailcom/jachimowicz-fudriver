package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.repository.ZarobkiRepository;

import java.util.Date;

/**
 * Created by marcin on 07.06.17.
 */
public class CreateZarobkiUT {

    private final ZarobkiRepository zarobkiRepository;
    private final BeanValidator beanValidator;


    public CreateZarobkiUT(ZarobkiRepository zarobkiRepository, BeanValidator beanValidator) {
        this.zarobkiRepository = zarobkiRepository;
        this.beanValidator = beanValidator;
    }

    public ZarobkiEntry createExport(Type type, Person person, Date day) {
        ZarobkiEntry zarobki = new ZarobkiEntry.ZarobkiEntryBuilder(zarobkiRepository, beanValidator)
                .cycleId(1L)
                .export(type.getWeight())
                .ilosc(type.getWeight())
                .pickerId(person.getId())
                .payed(false)
                .rodzajId(type.getId())
                .harvestTime(day)
                .build();
        zarobki.create();
        return zarobki;

    }
}
