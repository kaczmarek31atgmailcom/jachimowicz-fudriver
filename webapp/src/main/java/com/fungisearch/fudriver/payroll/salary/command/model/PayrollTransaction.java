package com.fungisearch.fudriver.payroll.salary.command.model;


import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.user.command.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payroll_transaction")
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PayrollTransaction extends BaseEntity{


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id",nullable = false)
    Person person;

    @Column(name = "person_name")
    String personName;

    @Column(name = "person_surname")
    String personSurname;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    Date date;

    @ManyToOne
    @JoinColumn(name = "creator_id",nullable = false)
    User creator;

    @Column(name = "creator_login")
    String creatorLogin;

    @Column(name = "creator_name")
    String creatorName;

    @Column(name = "creator_surname")
    String creatorSurname;

    @Column(name = "amount")
    Long amount;

    @ManyToOne
    @JoinColumn(name = "salary_header_id")
    MonthlyPayoffHeader payoffHeader;


    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonSurname() {
        return personSurname;
    }

    public Date getDate() {
        return date;
    }

    public User getCreator() {
        return creator;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorSurname() {
        return creatorSurname;
    }

    public Long getAmount() {
        return amount;
    }

    public MonthlyPayoffHeader getHeader() {
        return payoffHeader;
    }
}
