package com.fungisearch.fudriver.person.person.command.repository;

import com.fungisearch.fudriver.person.person.command.model.ForeignerAlert;
import com.fungisearch.fudriver.person.person.command.model.Person;

import java.util.List;

/**
 * Created by idea on 29.02.16.
 */
public interface PersonRepository {

    void save(Person person);
    Person find(Long id);
    Boolean isRfidUnique(String rfid);
    Person findPersonByRfid(String rfid);
    List<Person> findAll();
    List<Long> findReservedNumbers();

    ForeignerAlert getForeignerAlert();
}
