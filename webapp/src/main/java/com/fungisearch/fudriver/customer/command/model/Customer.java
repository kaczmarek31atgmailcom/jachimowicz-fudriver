package com.fungisearch.fudriver.customer.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.customer.command.repository.CustomerRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

/**
 * Created by marcin on 19.01.17.
 */
@Entity
@Table(name = "skrz_cust")
public class Customer extends BaseEntity {

    public transient CustomerRepository customerRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "producer_group_id")
    private ProducerGroup producerGroup;

    private Customer() {
    }

    public Customer(CustomerRepository customerRepository, BeanValidator beanValidator) {
        this.customerRepository = customerRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Boolean isActive() {
        return isActive;
    }

    public long create() {
        this.isActive = true;
        beanValidator.validate(this);
        customerRepository.save(this);
        return this.id;
    }

    public void delete(){
        this.isActive = false;
        beanValidator.validate(this);
    }

    public void edit(Edit edit) {
        this.name = edit.name;
        this.address = edit.address;
        beanValidator.validate(this);
    }

    public static class Edit {
        private String name;
        private String address;

        public Edit name(String name) {
            this.name = name;
            return this;
        }

        public Edit address(String address) {
            this.address = address;
            return this;
        }
    }


    public static class CustomerBuilder {
        private CustomerRepository customerRepository;
        private BeanValidator beanValidator;
        private String name;
        private String address;

        public CustomerBuilder(CustomerRepository customerRepository, BeanValidator beanValidator) {
            this.customerRepository = customerRepository;
            this.beanValidator = beanValidator;
        }

        public CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder address(String address) {
            this.address = address;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer(customerRepository, beanValidator);
            customer.name = name;
            customer.address = address;
            return customer;
        }
    }
}
