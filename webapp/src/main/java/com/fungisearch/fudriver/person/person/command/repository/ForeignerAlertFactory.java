package com.fungisearch.fudriver.person.person.command.repository;

import com.fungisearch.fudriver.person.person.command.model.ForeignerAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForeignerAlertFactory {
    private final PersonRepository personRepository;

    @Autowired
    public ForeignerAlertFactory(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ForeignerAlert getForeingerAlert(){
        return personRepository.getForeignerAlert();
    }
}
