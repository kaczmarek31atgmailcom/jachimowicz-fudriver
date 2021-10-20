package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.model.TimeSheet;
import com.fungisearch.fudriver.person.person.command.model.TimeSheetFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by idea on 04.03.16.
 */
@Service
@Transactional
public class AddPersonCommandHandler  {

    @Autowired
    private PersonFactory personFactory;

    @Autowired
    private TimeSheetFactory timeSheetFactory;

    public CommandResult handle(AddPersonCommand command) {

        Person person = personFactory.builder()
                .name(command.imie)
                .surname(command.nazwisko)
                .nr(command.nr)
                .adress(command.adres)
                .mobile(command.mobile)
                .groupId(command.groupId)
                .birthDate(command.birthDate)
                .city(command.city)
                .rejon(command.rejon)
                .indeks(command.indeks)
                .imionaRodzicow(command.imionaRodzicow)
                .nrWizy(command.nrWizy)
                .koniecWaznosciWizy(command.koniecWaznosciWizy)
                .pesel(command.pesel)
                .poczatekWaznosciZezwolenia(command.poczatekWaznosciZezwolenia)
                .koniecWaznosciZezwolenia(command.koniecWaznosciZezwolenia)
                .poczatekZameldowania(command.dataZameldowania)
                .koniecZameldowania(command.dataWymeldowania)
                .koniecWaznosciPaszportu(command.koniecWaznosciPaszportu)
                .passportNo(command.nrPaszportu)
                .statementNo(command.nrOswiadczenia)
                .permitNo(command.nrZezwolenia)
                .isForeigner(command.isForeigner)
                .build();
        Long personId = person.create();

        TimeSheet.TimeSheetBuilder timeSheetBuilder = timeSheetFactory.builder();
        TimeSheet timeSheet = timeSheetBuilder.personId(personId).build();
        timeSheet.openPeriod();

        return new CommandResult(personId, CommandResult.Status.OK,"Employee created");
    }
}
