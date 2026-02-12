package com.fungisearch.fudriver.person.person.command.model;

import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.repository.ZarobkiRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PersonFactory {

    private final PersonRepository personRepository;
    private final BeanValidator beanValidator;
    private final ZarobkiRepository zarobkiRepository;
    private final TimeWorkLogFactory timeWorkLogFactory;

    @Autowired
    public PersonFactory(PersonRepository personRepository, BeanValidator beanValidator, ZarobkiRepository zarobkiRepository, TimeWorkLogFactory timeWorkLogFactory) {
        this.personRepository = personRepository;
        this.beanValidator = beanValidator;
        this.zarobkiRepository = zarobkiRepository;
        this.timeWorkLogFactory = timeWorkLogFactory;
    }

    public Person find(Long id){
        Person person = personRepository.find(id);
        if(person != null){
            person.setPersonRepository(personRepository);
            person.setBeanValidator(beanValidator);
        }
        return person;
    }


    public Long findFirstNotReservedNumber(){
        List<Long> numbers = personRepository.findReservedNumbers();
        Collections.sort(numbers);
        long result = 1;
        for(Long number: numbers){
            if(number.equals(result)){
                result++;
            } else {
                break;
            }
        }
    return result;
    }

    public Person create(){
        Person person = new Person(this.personRepository, this.beanValidator);
        return person;
    }

    public List<Person> findAll(){
        List<Person> people = personRepository.findAll();
        for(Person person: people){
            person.setBeanValidator(beanValidator);
            person.setPersonRepository(personRepository);
        }
        return people;
    }

    public Person createByRfid(String rfid){
        Person person = personRepository.findPersonByRfid(rfid);
        if(person !=null){
            person.setBeanValidator(beanValidator);
            person.setPersonRepository(personRepository);
        }
        return person;
    }

    public Person.PersonBuilder builder(){
        Person.PersonBuilder personBuilder = new Person.PersonBuilder(personRepository,beanValidator);
        return personBuilder;
    }

    public List<Person> findAllByMonth(PayrollMonth month) {
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyyMM");
        Long timeshort = Long.parseLong(df.print(new DateTime(month.getFirstDay())));
        List<Long> zarobkiIds = zarobkiRepository.getLudzieIdsInMonth(timeshort);
        List<Person> timePeople = timeWorkLogFactory.findAllPeopleInMonth(new DateTime(month.getFirstDay()));
        List<Person> harvestPeople = new ArrayList<>();
        for(Long personId: zarobkiIds){
            Person person = personRepository.find(personId);
            person.setPersonRepository(personRepository);
            person.setBeanValidator(beanValidator);
            harvestPeople.add(person);
        }
        Set<Person> people = new HashSet<>();
        people.addAll(timePeople);
        people.addAll(harvestPeople);
        return new ArrayList<>(people);
    }
}

