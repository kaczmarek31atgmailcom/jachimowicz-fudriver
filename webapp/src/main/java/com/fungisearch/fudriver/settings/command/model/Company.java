package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.settings.command.repository.SettingsRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

/**
 * Created by marcin on 14.03.17
 */
@Entity
@Table(name = "company")
public class Company extends BaseEntity {

    public transient SettingsRepository settingsRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    protected Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "nip")
    private String nip;

    @Column(name = "regon")
    private String regon;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "email")
    private String email;

    @Column(name = "ggn")
    private String ggn;

    public Company(SettingsRepository settingsRepository, BeanValidator beanValidator) {
        this.settingsRepository = settingsRepository;
        this.beanValidator = beanValidator;
    }

    private Company(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getNip() {
        return nip;
    }

    public String getRegon() {
        return regon;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getGgn() {
        return ggn;
    }

    private void save(){
        beanValidator.validate(this);
        settingsRepository.saveCompany(this);
    }

    public void edit(Edit edit) {
        if(edit.name != null){
            this.name = edit.name;
        }
        if(edit.street != null){
            this.street = edit.street;
        }
        if(edit.city != null){
            this.city = edit.city;
        }
        if(edit.nip != null){
            this.nip = edit.nip;
        }
        if(edit.regon != null){
            this.regon = edit.regon;
        }
        if(edit.phoneNo != null){
            this.phoneNo = edit.phoneNo;
        }
        if(edit.email != null){
            this.email = edit.email;
        }

        if(edit.ggn != null){
            this.ggn = edit.ggn;
        }
        beanValidator.validate(this);
    }

    public static class Edit {
        private String name;
        private String street;
        private String city;
        private String nip;
        private String regon;
        private String phoneNo;
        private String email;
        private String ggn;

        public Edit name(String name) {
            this.name = name;
            return this;
        }

        public Edit street(String street) {
            this.street = street;
            return this;
        }

        public Edit city(String city) {
            this.city = city;
            return this;
        }

        public Edit nip(String nip) {
            this.nip = nip;
            return this;
        }

        public Edit regon(String regon){
            this.regon = regon;
            return this;
        }

        public Edit phoneNo(String phoneNo){
            this.phoneNo = phoneNo;
            return this;
        }

        public Edit email(String email){
            this.email = email;
            return this;
        }

        public Edit ggn(String ggn){
            this.ggn = ggn;
            return this;
        }
    }

    public static class CompanyBuilder{
        private SettingsRepository settingsRepository;
        private BeanValidator beanValidator;
        private String name;
        private String street;
        private String city;
        private String nip;
        private String regon;
        private String phoneNo;
        private String email;
        private String ggn;

        public CompanyBuilder(SettingsRepository settingsRepository, BeanValidator beanValidator) {
            this.settingsRepository = settingsRepository;
            this.beanValidator = beanValidator;
        }

        public CompanyBuilder name(String name){
            this.name = name;
            return this;
        }

        public CompanyBuilder street(String street){
            this.street = street;
            return this;
        }

        public CompanyBuilder city(String city){
            this.city = city;
            return this;
        }

        public CompanyBuilder nip(String nip){
            this.nip = nip;
            return this;
        }

        public CompanyBuilder regon(String regon){
            this.regon = regon;
            return this;
        }

        public CompanyBuilder phoneNo(String phoneNo){
            this.phoneNo = phoneNo;
            return this;
        }

        public CompanyBuilder email(String email){
            this.email = email;
            return this;
        }

        public CompanyBuilder ggn(String ggn){
            this.ggn = ggn;
            return this;
        }

        public Company build(){
            Company company = new Company(settingsRepository,beanValidator);
            company.city = city;
            company.email = email;
            company.ggn = ggn;
            company.name = name;
            company.phoneNo = phoneNo;
            company.nip = nip;
            company.regon = regon;
            company.street = street;
            company.save();
            return company;
        }
    }
}
