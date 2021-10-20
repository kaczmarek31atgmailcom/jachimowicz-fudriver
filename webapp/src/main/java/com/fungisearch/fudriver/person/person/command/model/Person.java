package com.fungisearch.fudriver.person.person.command.model;

import com.fungisearch.fudriver.exception.RfidDuplicationException;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Pracownik zarabiający stawkę godzinową
 */
@Entity
@Table(name = "ludzie")
public class Person {

    private transient PersonRepository personRepository;

    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    public Long id;

    @Column(name = "imie")
    private String name;

    @Column(name = "nazwisko")
    private String surname;

    @Column(name = "adres")
    private String adress;

    @Column(name = "nr_telefonu")
    private String mobile;

    @Column(name = "nr")
    private Long nr;

    @Column(name = "grupa")
    private Long groupId;

    @Column(name = "password")
    private String password;

    @Column(name = "data_urodzenia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(name = "miasto")
    private String city;

    @Column(name = "obwod")
    private String obwod;

    @Column(name = "rejon")
    private String rejon;

    @Column(name = "indeks")
    private String indeks;

    @Column(name = "imiona_rodzicow")
    private String imionaRodzicow;

    @Column(name = "nr_wizy")
    private String nrWizy;

    @Column(name = "koniec_waznosci_wizy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date koniecWaznosciWizy;

    @Column(name = "poczatek_waznosci_zezwolenia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date poczatekWaznosciZezwolenia;

    @Column(name = "koniec_waznosci_zezwolenia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date koniecWaznosciZezwolenia;

    @Column(name = "poczatek_zameldowania")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date poczatekZameldowania;

    @Column(name = "koniec_zameldowania")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date koniecZameldowania;

    @Column(name = "koniec_waznosci_paszportu")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date koniecWaznosciPaszportu;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "czy_akord")
    @Enumerated(EnumType.ORDINAL)
    private PayrollTypeEnum payrollType;

    @Column(name = "numer_paszportu")
    private String passportNo;

    @Column(name = "numer_oswiadczenia")
    private String statementNo;

    @Column(name = "numer_zezwolenia")
    private String permitNo;

    @Column(name = "rfid")
    private String rfid;

    @Column(name = "active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @NotNull
    private Boolean active;

    @Column(name = "czy_obcokrajowiec")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @NotNull
    private Boolean isForeigner;

    @Column(name = "regular_wage")
    private Long regularWage;

    @Column(name = "sunday_wage")
    private Long sundayWage;

    @Column(name = "bonus_wage")
    private Long bonusWage;

    @ManyToOne
    @JoinColumn(name = "wage_header_id")
    private WageHeader wageHeader;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getVersion() {
        return version;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAdress() {
        return adress;
    }

    public String getMobile() {
        return mobile;
    }

    public Long getNr() {
        return nr;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    public String getObwod() {
        return obwod;
    }

    public String getRejon() {
        return rejon;
    }

    public String getIndeks() {
        return indeks;
    }

    public String getImionaRodzicow() {
        return imionaRodzicow;
    }

    public String getNrWizy() {
        return nrWizy;
    }

    public Date getKoniecWaznosciWizy() {
        return koniecWaznosciWizy;
    }

    public Date getPoczatekWaznosciZezwolenia() {
        return poczatekWaznosciZezwolenia;
    }

    public Date getPoczatekZameldowania() {
        return poczatekZameldowania;
    }

    public Date getKoniecZameldowania() {
        return koniecZameldowania;
    }

    public Date getKoniecWaznosciPaszportu() {
        return koniecWaznosciPaszportu;
    }

    public Boolean getForeigner() {
        return isForeigner;
    }

    public Date getKoniecWaznosciZezwolenia() {
        return koniecWaznosciZezwolenia;
    }

    public String getPesel() {
        return pesel;
    }

    public PayrollTypeEnum getPayrollType() {
        return payrollType;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public String getStatementNo() {
        return statementNo;
    }

    public String getPermitNo() {
        return permitNo;
    }

    public String getRfid() {
        return rfid;
    }

    public Boolean getActive() {
        return active;
    }

    public Long getRegularWage() {
        return regularWage;
    }

    public Long getSundayWage() {
        return sundayWage;
    }

    public Long getBonusWage() {
        return bonusWage;
    }

    public WageHeader getWageHeader() {
        return wageHeader;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    @SuppressWarnings("unused")
    public Person() {
    }

    public Person(PersonRepository personRepository, BeanValidator beanValidator) {
        this.personRepository = personRepository;
        this.beanValidator = beanValidator;
    }

    public void registerBadge(){
        if(!personRepository.isRfidUnique(this.rfid)){
            throw new RfidDuplicationException();
        }
        Person person = personRepository.find(this.id);
        person.rfid = this.rfid;
        person.version = this.version;
        beanValidator.validate(person);
    }

    public void deleteBadge(){
        Person person = personRepository.find(this.id);
        person.rfid = null;
        person.version = this.version;
        beanValidator.validate(person);
    }


    public void activate(Long number) {
        this.nr = number;
        this.active = true;
        beanValidator.validate(this);
    }

    public void changePassword(String password){
        Person person = personRepository.find(this.id);
        person.password = DigestUtils.md5Hex(password);
        beanValidator.validate(person);
    }


    public void inactivate() {
        this.nr = 0L;
        this.active = false;
        beanValidator.validate(this);
    }

    public void setWageHeader(WageHeader wageHeader){
        this.wageHeader = wageHeader;
        beanValidator.validate(this);
    }

    public void switchToAccordAccordPayed(){
        this.payrollType = PayrollTypeEnum.ACCORD;
        beanValidator.validate(this);
    }

    public void switchToHourlyPayed(){
        this.payrollType = PayrollTypeEnum.HOURLY;
        beanValidator.validate(this);
    }

    public void setRegularWage(long wage){
        this.regularWage = wage;
        beanValidator.validate(this);
    }

    public void setSundayWage(long wage){
        this.sundayWage = wage;
        beanValidator.validate(this);
    }

    public void setBonusWage(long wage){
        this.bonusWage = wage;
        beanValidator.validate(this);
    }

    public Long create(){
        this.active = true;
        if(payrollType == null){
            payrollType = PayrollTypeEnum.ACCORD;
        }
        beanValidator.validate(this);
        personRepository.save(this);
        return this.id;
    }

    public void edit(Edit edit) {
        this.nr = edit.nr;
        this.name = edit.name;
        this.surname = edit.surname;
        this.adress = edit.adress;
        this.mobile = edit.mobile;
        this.groupId = edit.groupId;
        this.birthDate = edit.birthDate;
        this.city = edit.city;
        this.rejon = edit.rejon;
        this.indeks = edit.indeks;
        this.imionaRodzicow = edit.imionaRodzicow;
        this.nrWizy = edit.nrWizy;
        this.koniecWaznosciWizy = edit.koniecWaznosciWizy;
        this.poczatekWaznosciZezwolenia = edit.poczatekWaznosciZezwolenia;
        this.koniecWaznosciZezwolenia = edit.koniecWaznosciZezwolenia;
        this.poczatekZameldowania = edit.poczatekZameldowania;
        this.koniecZameldowania = edit.koniecZameldowania;
        this.koniecWaznosciPaszportu = edit.koniecWaznosciPaszportu;
        this.pesel = edit.pesel;
        this.payrollType = edit.payrollType;
        this.passportNo = edit.passportNo;
        this.statementNo = edit.statementNo;
        this.permitNo = edit.permitNo;

        this.rfid = edit.rfid;
        this.active = edit.active;
        this.isForeigner = edit.isForeigner;
        this.version = edit.version;
        beanValidator.validate(this);
    }


    public static class Edit {
        private Long nr;
        private String name;
        private String surname;
        private String adress;
        private String mobile;
        private Long groupId;
        private Date birthDate;
        private String city;
        private String rejon;
        private String indeks;
        private String imionaRodzicow;
        private String nrWizy;
        private Date koniecWaznosciWizy;
        private Date poczatekWaznosciZezwolenia;
        private Date koniecWaznosciZezwolenia;
        private Date poczatekZameldowania;
        private Date koniecZameldowania;
        private Date koniecWaznosciPaszportu;
        private String pesel;
        private PayrollTypeEnum payrollType;
        private String passportNo;
        private String statementNo;
        private String permitNo;
        private String rfid;
        private Boolean isForeigner;
        private Boolean active;
        private Long version;

        public Edit nr(Long nr) {
            this.nr = nr;
            return this;
        }

        public Edit name(String name) {
            this.name = name;
            return this;
        }

        public Edit surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Edit adress(String adress) {
            this.adress = adress;
            return this;
        }

        public Edit mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Edit groupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public Edit birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Edit city(String city) {
            this.city = city;
            return this;
        }

        public Edit rejon(String rejon) {
            this.rejon = rejon;
            return this;
        }

        public Edit indeks(String indeks) {
            this.indeks = indeks;
            return this;
        }

        public Edit imionaRodzicow(String imionaRodzicow) {
            this.imionaRodzicow = imionaRodzicow;
            return this;
        }

        public Edit nrWizy(String nrWizy) {
            this.nrWizy = nrWizy;
            return this;
        }

        public Edit koniecWaznosciWizy(Date koniecWaznosciWizy){
            this.koniecWaznosciWizy = koniecWaznosciWizy;
            return this;
        }

        public Edit poczatekWaznosciZezwolenia(Date  poczatekWaznosciZezwolenia){
            this.poczatekWaznosciZezwolenia = poczatekWaznosciZezwolenia;
            return this;
        }

        public Edit koniecWaznosciZezwolenia(Date koniecWaznosciZezwolenia) {
            this.koniecWaznosciZezwolenia = koniecWaznosciZezwolenia;
            return this;
        }

        public Edit poczatekZameldowania(Date poczatekZameldowania){
            this.poczatekZameldowania = poczatekZameldowania;
            return this;
        }

        public Edit koniecZameldowania(Date koniecZameldowania){
            this.koniecZameldowania = koniecZameldowania;
            return this;
        }

        public Edit koniecWaznosciPaszportu(Date koniecWaznosciPaszportu){
            this.koniecWaznosciPaszportu = koniecWaznosciPaszportu;
            return this;
        }

        public Edit pesel(String pesel) {
            this.pesel = pesel;
            return this;
        }

        public Edit payrollType(PayrollTypeEnum payrollType) {
            this.payrollType = payrollType;
            return this;
        }

        public Edit passportNo(String passportNo) {
            this.passportNo = passportNo;
            return this;
        }

        public Edit statementNo(String statementNo) {
            this.statementNo = statementNo;
            return this;
        }

        public Edit permitNo(String permitNo) {
            this.permitNo = permitNo;
            return this;
        }

        public Edit rfid(String rfid) {
            this.rfid = rfid;
            return this;
        }

        public Edit active(Boolean active) {
            this.active = active;
            return this;
        }

        public Edit isForeigner(Boolean isForeigner) {
            this.isForeigner = isForeigner;
            return this;
        }

        public Edit version(Long version) {
            this.version = version;
            return this;
        }
    }

    public static class PersonBuilder {
        private PersonRepository personRepository;
        private BeanValidator beanValidator;
        private Long id;
        private Long nr;
        private String name;
        private String surname;
        private String adress;
        private String mobile;
        private Long groupId;
        private Date birthDate;
        private String city;
        private String rejon;
        private String indeks;
        private String imionaRodzicow;
        private String nrWizy;
        private Date koniecWaznosciWizy;
        private Date poczatekWaznosciZezwolenia;
        private Date koniecWaznosciZezwolenia;
        private Date poczatekZameldowania;
        private Date koniecZameldowania;
        private Date koniecWaznosciPaszportu;
        private String pesel;
        private PayrollTypeEnum payrollType;
        private String passportNo;
        private String statementNo;
        private String permitNo;
        private String rfid;
        private Boolean active;
        private Boolean isForeigner;
        private WageHeader wageHeader;
        private Long version;

        public PersonBuilder(PersonRepository personRepository, BeanValidator beanValidator) {
            this.beanValidator = beanValidator;
            this.personRepository = personRepository;
        }

        public PersonBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PersonBuilder nr(Long nr) {
            this.nr = nr;
            return this;
        }

        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public PersonBuilder adress(String adress) {
            this.adress = adress;
            return this;
        }

        public PersonBuilder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public PersonBuilder groupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public PersonBuilder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public PersonBuilder city(String city) {
            this.city = city;
            return this;
        }

        public PersonBuilder rejon(String rejon) {
            this.rejon = rejon;
            return this;
        }

        public PersonBuilder indeks(String indeks) {
            this.indeks = indeks;
            return this;
        }

        public PersonBuilder imionaRodzicow(String imionaRodzicow) {
            this.imionaRodzicow = imionaRodzicow;
            return this;
        }

        public PersonBuilder nrWizy(String nrWizy) {
            this.nrWizy = nrWizy;
            return this;
        }

        public PersonBuilder koniecWaznosciWizy(Date koniecWaznosciWizy){
            this.koniecWaznosciWizy = koniecWaznosciWizy;
            return this;
        }

        public PersonBuilder poczatekWaznosciZezwolenia(Date poczatekWaznosciZezwolenia){
            this.poczatekWaznosciZezwolenia = poczatekWaznosciZezwolenia;
            return this;
        }

        public PersonBuilder koniecWaznosciZezwolenia(Date koniecWaznosciZezwolenia) {
            this.koniecWaznosciZezwolenia = koniecWaznosciZezwolenia;
            return this;
        }

        public PersonBuilder poczatekZameldowania(Date poczatekZameldowania){
            this.poczatekZameldowania = poczatekZameldowania;
            return this;
        }

        public PersonBuilder koniecZameldowania(Date koniecZameldowania){
            this.koniecZameldowania = koniecZameldowania;
            return this;
        }

        public PersonBuilder koniecWaznosciPaszportu(Date koniecWaznosciPaszportu){
            this.koniecWaznosciPaszportu = koniecWaznosciPaszportu;
            return this;
        }


        public PersonBuilder pesel(String pesel) {
            this.pesel = pesel;
            return this;
        }

        public PersonBuilder payrollType(PayrollTypeEnum payrollType) {
            this.payrollType = payrollType;
            return this;
        }

        public PersonBuilder passportNo(String passportNo) {
            this.passportNo = passportNo;
            return this;
        }

        public PersonBuilder statementNo(String statementNo) {
            this.statementNo = statementNo;
            return this;
        }

        public PersonBuilder permitNo(String permitNo){
            this.permitNo = permitNo;
            return this;
        }

        public PersonBuilder rfid(String rfid) {
            this.rfid = rfid;
            return this;
        }

        public PersonBuilder wageHeader(WageHeader wageHeader){
            this.wageHeader = wageHeader;
            return this;
        }

        public PersonBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public PersonBuilder isForeigner (Boolean isForeigner) {
            this.isForeigner = isForeigner;
            return this;
        }
        public PersonBuilder version(Long version) {
            this.version = version;
            return this;
        }

        public Person build() {
            Person person = new Person(this.personRepository, this.beanValidator);
            person.id = this.id;
            person.nr = this.nr;
            person.name = this.name;
            person.surname = this.surname;
            person.adress = this.adress;
            person.mobile = this.mobile;
            person.groupId = this.groupId;
            person.birthDate = this.birthDate;
            person.city = this.city;
            person.rejon = this.rejon;
            person.indeks = this.indeks;
            person.imionaRodzicow = this.imionaRodzicow;
            person.nrWizy = this.nrWizy;
            person.koniecWaznosciWizy = this.koniecWaznosciWizy;
            person.poczatekWaznosciZezwolenia = this.poczatekWaznosciZezwolenia;
            person.koniecWaznosciZezwolenia = this.koniecWaznosciZezwolenia;
            person.poczatekZameldowania = this.poczatekZameldowania;
            person.koniecZameldowania = this.koniecZameldowania;
            person.koniecWaznosciPaszportu = this.koniecWaznosciPaszportu;
            person.pesel = this.pesel;
            person.payrollType = this.payrollType;
            person.passportNo = this.passportNo;
            person.statementNo = this.statementNo;
            person.permitNo = this.permitNo;
            person.rfid = this.rfid;
            person.regularWage = 0L;
            person.sundayWage = 0L;
            person.bonusWage = 0L;
            person.wageHeader = wageHeader;
            person.isForeigner = isForeigner;
            person.active = this.active;
            person.version = this.version;
            return person;
        }
    }
}