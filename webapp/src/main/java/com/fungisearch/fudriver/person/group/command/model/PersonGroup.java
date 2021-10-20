package com.fungisearch.fudriver.person.group.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.person.group.command.repository.PersonGroupRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

/**
 * Created by marcin on 04.01.17.
 */
@Entity
@Table(name = "grupy")
public class PersonGroup extends BaseEntity{

    public transient PersonGroupRepository personGroupRepository;
    public transient BeanValidator beanValidator;

    @GeneratedValue
    @Id
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name = "active")
    private Boolean isActive;

    private PersonGroup(){}

    public PersonGroup(PersonGroupRepository personGroupRepository, BeanValidator beanValidator){
        this.personGroupRepository = personGroupRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean isActive() {
        return isActive;
    }

    public Long create(){
        beanValidator.validate(this);
        personGroupRepository.save(this);
        return this.id;
    }

    public static class PersonGroupBuilder{
        private PersonGroupRepository personGroupRepository;
        private BeanValidator beanValidator;
        private String name;

        public PersonGroupBuilder(PersonGroupRepository personGroupRepository, BeanValidator beanValidator){
            this.personGroupRepository = personGroupRepository;
            this.beanValidator = beanValidator;
        }

        public PersonGroupBuilder name(String name){
            this.name = name;
            return this;
        }

        public PersonGroup build(){
            PersonGroup personGroup = new PersonGroup(personGroupRepository,beanValidator);
            personGroup.name = this.name;
            personGroup.isActive = true;
            return personGroup;
        }
    }
}
