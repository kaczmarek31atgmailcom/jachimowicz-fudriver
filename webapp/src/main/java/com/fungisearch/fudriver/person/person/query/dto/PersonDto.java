package com.fungisearch.fudriver.person.person.query.dto;

import java.util.Date;

/**
 * Created by idea on 29.02.16.
 */
public class PersonDto {
    public Long id;
    public Long nr;
    public String imie;
    public String nazwisko;
    public String adres;
    public String mobile;
    public Long groupId;
    public String groupName;
    public Date birthDate;
    public String city;
    public String rejon;
    public String indeks;
    public String imionaRodzicow;
    public Date dataZameldowania;
    public Date dataWymeldowania;
    public String nrWizy;
    public Date koniecWaznosciWizy;
    public String pesel;
    public int payrollStatus;
    public String nrPaszportu;
    public Date koniecWaznosciPaszportu;
    public String nrOswiadczenia;
    public String nrZezwolenia;
    public Date koniecWaznosciZezwolenia;
    public Date poczatekWaznosciZezwolenia;
    public String rfid;
    public Boolean active;
    public boolean isForeigner = false;
    public Long version;
}
