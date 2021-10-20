package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollTransactionRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity(name = "PayrollDecreaseTransaction")
@DiscriminatorValue("decrease")
public class PayrollDecreaseTransaction extends PayrollTransaction{
    transient PayrollTransactionRepository payrollTransactionRepository;
    transient BeanValidator beanValidator;

    private PayrollDecreaseTransaction(){}

    public PayrollDecreaseTransaction(PayrollTransactionRepository payrollTransactionRepository, BeanValidator beanValidator) {
        this.payrollTransactionRepository = payrollTransactionRepository;
        this.beanValidator = beanValidator;
    }

    private void create(){
        beanValidator.validate(this);
        payrollTransactionRepository.save(this);
    }

    public static class PayrollDecreaseTransactionBuilder{
        private PayrollTransactionRepository payrollTransactionRepository;
        private BeanValidator beanValidator;
        private Person person;
        private User creator;
        private Long amountMoney;

        public PayrollDecreaseTransactionBuilder(PayrollTransactionRepository payrollTransactionRepository, BeanValidator beanValidator) {
            this.payrollTransactionRepository = payrollTransactionRepository;
            this.beanValidator = beanValidator;
        }

        public PayrollDecreaseTransactionBuilder person(Person person){
            this.person = person;
            return this;
        }

        public PayrollDecreaseTransactionBuilder creator(User creator){
            this.creator = creator;
            return this;
        }

        public PayrollDecreaseTransactionBuilder amountMoney(long amountMoney){
            this.amountMoney = amountMoney;
            return this;
        }

        public PayrollDecreaseTransaction build(){
            PayrollDecreaseTransaction transaction = new PayrollDecreaseTransaction(payrollTransactionRepository,beanValidator);
            transaction.person = person;
            transaction.personName = person.getName();
            transaction.personSurname = person.getSurname();
            transaction.creator = creator;
            transaction.creatorLogin = creator.getLogin();
            transaction.creatorName = creator.getName();
            transaction.creatorSurname = creator.getSurname();
            transaction.date = new Date();
            transaction.amount = amountMoney * -1;
            transaction.create();
            return transaction;
        }
    }

}
