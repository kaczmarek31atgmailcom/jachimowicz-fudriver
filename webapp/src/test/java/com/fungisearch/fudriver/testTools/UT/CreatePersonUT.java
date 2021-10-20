package com.fungisearch.fudriver.testTools.UT;


import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

public class CreatePersonUT {

    private final PersonRepository personRepository;
    private final BeanValidator beanValidator;

    public CreatePersonUT(PersonRepository personRepository, BeanValidator beanValidator) {
        this.personRepository = personRepository;
        this.beanValidator = beanValidator;
    }

    public Person create(PayrollTypeEnum payrollType,String name, String surname){
        Person person = new Person.PersonBuilder(personRepository, beanValidator)
                .active(true)
                .groupId(1L)
                .payrollType(payrollType)
                .id(1L)
                .name(name)
                .surname(surname)
                .version(1L)
                .build();
        person.create();
        return person;
    }
}
