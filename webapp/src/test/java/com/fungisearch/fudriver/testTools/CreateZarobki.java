package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateZarobki {
    private final CreateUser createUser;
    private final CreateType createType;
    private final CreateCycle createCycle;
    private final ZarobkiFactory zarobkiFactory;

    @Autowired
    public CreateZarobki(CreateUser createUser, CreateType createType, CreateCycle createCycle, ZarobkiFactory zarobkiFactory) {
        this.createUser = createUser;
        this.createType = createType;
        this.createCycle = createCycle;
        this.zarobkiFactory = zarobkiFactory;
    }

    public ZarobkiEntry create() {
        Type type = createType.create();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = formatter.parseDateTime("2017-02-02");
        ZarobkiEntry zarobkiEntry = zarobkiFactory
                .builder()
                .cycleId(createCycle.create().getId())
                .export(type.getWeight())
                .ilosc(type.getWeight())
                .rodzajId(type.getId())
                .payed(false)
                .harvestTime(dt.toDate())
                .timeshort(new Long(201702))
                .pickerId(createUser.create().getId())
                .trolleyId(new Long(123))
                .build();
        zarobkiEntry.create();
        return zarobkiEntry;
    }

    public ZarobkiEntry create(Type type) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = formatter.parseDateTime("2017-02-02");
        ZarobkiEntry zarobkiEntry = zarobkiFactory
                .builder()
                .cycleId(createCycle.create().getId())
                .export(type.getWeight())
                .ilosc(type.getWeight())
                .rodzajId(type.getId())
                .payed(false)
                .harvestTime(dt.toDate())
                .timeshort(new Long(201702))
                .pickerId(createUser.create().getId())
                .trolleyId(new Long(123))
                .build();
        zarobkiEntry.create();
        return zarobkiEntry;
    }

    public ZarobkiEntry create(Type type, Date day,Person person) {
        DateTime dateTime = new DateTime(day);
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyyMM");
        long timeShort = Long.parseLong(df.print(dateTime));
        ZarobkiEntry zarobkiEntry = zarobkiFactory
                .builder()
                .cycleId(createCycle.create().getId())
                .harvestTime(day)
                .export(type.getWeight())
                .ilosc(type.getWeight())
                .rodzajId(type.getId())
                .payed(false)
                .timeshort(timeShort)
                .pickerId(person.getId())
                .trolleyId(new Long(123))
                .build();
        zarobkiEntry.create();
        return zarobkiEntry;
    }
}
