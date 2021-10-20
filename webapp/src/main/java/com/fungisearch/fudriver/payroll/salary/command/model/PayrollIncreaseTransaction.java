package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollTransactionRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity(name = "PayrollIncreaseTransaction")
@DiscriminatorValue("increase")
public class PayrollIncreaseTransaction extends PayrollTransaction {
    transient PayrollTransactionRepository payrollTransactionRepository;
    transient BeanValidator beanValidator;

    private PayrollIncreaseTransaction(){}

    public PayrollIncreaseTransaction(PayrollTransactionRepository payrollTransactionRepository, BeanValidator beanValidator) {

        this.payrollTransactionRepository = payrollTransactionRepository;
        this.beanValidator = beanValidator;
    }

    private void create() {
        beanValidator.validate(this);
        payrollTransactionRepository.save(this);
    }


    public static class PayrollIncreaseTransactionBuilder {
        private PayrollTransactionRepository payrollTransactionRepository;
        private BeanValidator beanValidator;
        private Person person;
        private User creator;
        private Long amountMoney;
        private MonthlyPayoffHeader header;

        public PayrollIncreaseTransactionBuilder(PayrollTransactionRepository payrollTransactionRepository, BeanValidator beanValidator) {
            this.payrollTransactionRepository = payrollTransactionRepository;
            this.beanValidator = beanValidator;
        }

        public PayrollIncreaseTransactionBuilder person(Person person){
            this.person = person;
            return this;
        }

        public PayrollIncreaseTransactionBuilder creator(User creator){
            this.creator = creator;
            return this;
        }

        public PayrollIncreaseTransactionBuilder amountMoney(long amountMoney){
            this.amountMoney = amountMoney;
            return this;
        }

        public PayrollIncreaseTransactionBuilder header(MonthlyPayoffHeader header){
            this.header = header;
            return this;
        }

        public PayrollIncreaseTransaction build(){
            PayrollIncreaseTransaction transaction = new PayrollIncreaseTransaction(payrollTransactionRepository,beanValidator);
            transaction.person = person;
            transaction.personName = person.getName();
            transaction.personSurname = person.getSurname();
            transaction.creator = creator;
            transaction.creatorLogin = creator.getLogin();
            transaction.creatorName = creator.getName();
            transaction.creatorSurname = creator.getSurname();
            transaction.date = new Date();
            transaction.amount = amountMoney;
            transaction.payoffHeader = header;
            transaction.create();
            return transaction;
        }
    }


}
