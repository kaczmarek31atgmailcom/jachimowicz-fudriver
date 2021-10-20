package com.fungisearch.fudriver.person.person.command.model;

import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;

import javax.persistence.*;

@Entity
@Table(name = "foreigner_alert")
public class ForeignerAlert{

    private transient PersonRepository personRepository;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "visa_days")
    private int visaDays = 0;

    @Column(name = "statement_days")
    private int statementDays = 0;

    @Column(name = "passport_days")
    private int passportDays = 0;

    @Column(name = "stay_days")
    private int stayDays = 0;

    private ForeignerAlert(){}

    public ForeignerAlert(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void edit(Edit edit){
        if(edit.visaDays != null){
            this.visaDays = edit.visaDays;
        }
        if(edit.statementDays != null){
            this.statementDays = edit.statementDays;
        }
        if(edit.passportDays != null){
            this.passportDays = edit.passportDays;
        }
        if(edit.stayDays != null){
            this.stayDays = edit.stayDays;
        }
    }

    public static class Edit{
        private Integer visaDays;
        private Integer statementDays;
        private Integer passportDays;
        private Integer stayDays;

        public Edit visaDays(Integer visaDays){
            if(visaDays != null) {
                this.visaDays = Math.abs(visaDays);
            }
            return this;
        }

        public Edit statementDays(Integer statementDays){
            if (statementDays != null) {
                this.statementDays = Math.abs(statementDays);
            }
            return this;
        }

        public Edit passportDays(Integer passportDays){
            if(passportDays != null) {
                this.passportDays = Math.abs(passportDays);
            }
            return this;
        }

        public Edit stayDays(Integer stayDays){
            if(stayDays != null) {
                this.stayDays = Math.abs(stayDays);
            }
            return this;
        }
    }

}
