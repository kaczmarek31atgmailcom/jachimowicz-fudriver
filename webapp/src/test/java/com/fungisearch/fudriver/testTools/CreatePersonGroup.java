package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.person.group.command.model.PersonGroup;
import com.fungisearch.fudriver.person.group.command.repository.PersonGroupRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

/**
 * Created by marcin on 15.01.17.
 */
public class CreatePersonGroup {

    private final PersonGroupRepository personGroupRepository;
    private final BeanValidator beanValidator;

    public CreatePersonGroup(PersonGroupRepository personGroupRepository, BeanValidator beanValidator){
        this.personGroupRepository = personGroupRepository;
        this.beanValidator = beanValidator;
    }

    public PersonGroup create(){
        PersonGroup personGroup = new PersonGroup.PersonGroupBuilder(personGroupRepository, beanValidator)
                .name("test_group")
                .build();
        personGroup.create();
        return personGroup;
    }
}
