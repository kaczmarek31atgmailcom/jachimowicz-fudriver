package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.person.group.command.model.PersonGroup;
import com.fungisearch.fudriver.person.group.command.repository.PersonGroupRepository;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 02.09.16.
 */
@Service
public class CreatePerson {
    public final PersonGroupRepository personGroupRepository;
    public final PersonRepository personRepository;
    public final BeanValidator beanValidator;

    @Autowired
    public CreatePerson(PersonGroupRepository personGroupRepository,
                        PersonRepository personRepository,
                        BeanValidator beanValidator) {
        this.personGroupRepository = personGroupRepository;
        this.personRepository = personRepository;
        this.beanValidator = beanValidator;
    }

    public Person create() {
        PersonGroup personGroup = new PersonGroup.PersonGroupBuilder(personGroupRepository, beanValidator)
                .name("test_group")
                .build();
        personGroup.create();

        Person person = new Person.PersonBuilder(personRepository, beanValidator)
                .name("test_name")
                .nr(new Long(1))
                .payrollType(PayrollTypeEnum.ACCORD)
                .groupId(personGroup.getId())
                .build();
        person.create();
        return person;
    }

    public Person create(WageHeader wageHeader) {
        PersonGroup personGroup = new PersonGroup.PersonGroupBuilder(personGroupRepository, beanValidator)
                .name("test_group")
                .build();
        personGroup.create();

        Person person = new Person.PersonBuilder(personRepository, beanValidator)
                .name("test_name")
                .nr(new Long(1))
                .payrollType(PayrollTypeEnum.ACCORD)
                .groupId(personGroup.getId())
                .wageHeader(wageHeader)
                .build();
        person.create();
        return person;
    }
}
