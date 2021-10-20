package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by idea on 29.02.16.
 */
@Service
@Transactional
public class EditPersonCommandHandler {

    @Autowired
    private PersonFactory personFactory;

    public CommandResult handle(EditPersonCommand command) {
        Person person = personFactory.find(command.id);

        person.edit(new Person.Edit()
                .nr(command.nr)
                .name(command.imie)
                .surname(command.nazwisko)
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
                .poczatekWaznosciZezwolenia(command.poczatekWaznosciZezwolenia)
                .koniecWaznosciZezwolenia(command.koniecWaznosciZezwolenia)
                .pesel(command.pesel)
                .payrollType(command.payrollType)
                .passportNo(command.nrPaszportu)
                .koniecWaznosciPaszportu(command.koniecWaznosciPaszportu)
                .statementNo(command.nrOswiadczenia)
                .permitNo(command.nrZezwolenia)
                .rfid(command.rfid)
                .isForeigner(command.isForeigner)
                .poczatekZameldowania(command.dataZameldowania)
                .koniecZameldowania(command.dataWymeldowania)
                .active(command.active)
                .version(command.version));
        return CommandResult.OK;
    }
}
